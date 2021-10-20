package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.service.api.MySQLRemoteService;
import com.atguigu.crowd.service.api.RedisRemoteService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Controller
public class MemberHandler {

    @Resource
    private RedisRemoteService redisRemoteService;
    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/auth/do/member/login")
    public String login(@RequestParam("loginacct") String loginacct,
                        @RequestParam("userpswd") String userpswd,
                        ModelMap modelMap,
                        HttpSession session) {
        // 调用远程接口通过登录账号查询MemberPO
        ResultEntity<MemberPO> memberPOResultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        // 判断是否失败
        if (ResultEntity.FAILED.equals(memberPOResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, memberPOResultEntity.getMsg());
            return "member-login";
        }
        MemberPO memberPO = memberPOResultEntity.getData();
        if (memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, memberPOResultEntity.getMsg());
            return "member-login";
        }
        // 比较密码
        String userPswdForm = memberPO.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(userpswd, userPswdForm);
        // 密码不正确的时候
        if (!matches) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        // 账号密码正确 将MemberLoginVO存入session域中
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
        return "redirect:http://www.crowd.com/auth/member/to/center/page";
    }

    @RequestMapping("/auth/do/member/reigster")
    public String register(MemberVO memberVO, ModelMap modelMap) {
        // 获取表单中输入的手机号
        String phoneNum = memberVO.getPhoneNum();
        // 拼redis中存储验证码的key
        String keyString = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        // 从redis中查询key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueRemoteByKey(keyString);
        // 检查查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMsg());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_VERIFICATION_CODE_IS_NOT_EQUAL);
            return "member-reg";
        }
        // 如果能从redis中查到value则把value和表单中的验证码做比较
        if (!Objects.equals(redisCode, memberVO.getCode())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.ERROR_VERIFICATION_CODE_IS_NOT_EQUAL);
            return "member-reg";
        }
        // 如果一致则删除验证码
        redisRemoteService.removeRedisKeyRemote(keyString);
        // 对密码进行加密
        String userpswdBefore = memberVO.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodeAfter = passwordEncoder.encode(userpswdBefore);
        memberVO.setUserpswd(encodeAfter);
        // 执行保存
        // 新建一个空的MemberPO
        MemberPO memberPO = new MemberPO();
        // 利用BeanUtils复制属性
        BeanUtils.copyProperties(memberVO, memberPO);
        ResultEntity<String> saveMember = mySQLRemoteService.saveMember(memberPO);
        if (ResultEntity.FAILED.equals(saveMember.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMember.getMsg());
            return "member-reg";
        }
        //  这里使用重定向 避免刷新浏览器  导致重新执行注册流程
        return "redirect:/member/auth/to/login/page";
    }

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message")
    public ResultEntity<String> sendShortMessage(@RequestParam("phone") String phone) {
        // 发送短信
        ResultEntity<String> shortMessage = CrowdUtil.sendCodeByShortMessage(phone);
        // 判断短信结果
        if (ResultEntity.SUCCESS.equals(shortMessage.getResult())) {
            // 获取短信
            String code = shortMessage.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX + phone;
            // 将验证码存入redis
            ResultEntity<String> resultEntity = redisRemoteService.setRedisKeyValueRemoteTimeout(key, code, 15, TimeUnit.MINUTES);
            //判断结果
            if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            }
            return resultEntity;
        } else {
            return shortMessage;
        }
    }
}
