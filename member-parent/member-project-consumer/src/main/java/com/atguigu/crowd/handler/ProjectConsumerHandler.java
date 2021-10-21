package com.atguigu.crowd.handler;

import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.vo.MemberConfirmInfoVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.entity.vo.ReturnVO;
import com.atguigu.crowd.service.api.MySQLRemoteService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * create by: zr
 * description: TODO
 * create time: 2021/10/21 10:27
 * 接受表单数据
 *
 * @Param: null
 * @return
 */
@Controller
public class ProjectConsumerHandler {
    @Resource
    private OSSProperties ossProperties;
    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/create/confirm")
    public String saveConfirm(
            ModelMap modelMap,
            HttpSession session,
            MemberConfirmInfoVO memberConfirmInfoVO) {
        // 从Session域中取出临时存储的projectVO对象
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        // 对取出的projectVO进行判空
        if (projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        // 将确认信息的数据保存到projectVO对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 从Session域中读取当前登录用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        // 调用远程方法保存projectVO对象
        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(projectVO, memberId);
        // 判断保存projectVO对象是否成功
        if (ResultEntity.FAILED.equals(saveResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveResultEntity.getMsg());
            return "project-confirm";
        }
        // 保存成功后从Session域中删除临时保存的数据
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        // 如果远程保存成功则跳转到最终完成页面
        return "redirect:http://www.crowd.com/project/create/success/page";
    }

    @ResponseBody
    @RequestMapping("/create/save/return")
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {
        try {
            // 从Session域中读取之前缓存到redis中的ProjectVO对象
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            // 判断projectVO是否为空
            if (projectVO == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }
            // 从projectVO获取存储回报信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            // 判断returnVOList是否有效
            if (returnVOList == null || returnVOList.size() == 0) {
                // 创建集合对returnVOList进行初始化
                returnVOList = new ArrayList<>();
                // 将returnVOList存入projectVO中
                projectVO.setReturnVOList(returnVOList);
            }
            // 将收集了表单数据的 returnVO 对象存入集合
            returnVOList.add(returnVO);
            // 把数据有变化的 ProjectVO 对象重新存入 Session 域，以确保新的数据最终能够 存入 Redis
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
            // 所有操作成功完成返回成功
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            // 如果有异常就返回异常消息
            return ResultEntity.failed(e.getMessage());
        }

    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture")

    public ResultEntity<String> uploadReturnPicture(
            // 请求上传的文件
            @RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {
        // 执行上传
        ResultEntity<String> uploadPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                returnPicture.getOriginalFilename());
        return uploadPicResultEntity;
    }

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(
            // 接受除了上传图片之外的普通数据
            ProjectVO projectVO,
            // 接受上传的头图
            MultipartFile headerPicture,
            // 接受上传的详情图片
            List<MultipartFile> detailPictureList,
            // 将收集了一部分数据的ProjectVO存入Session域中
            HttpSession session,
            // 用来在当前操作失败后返回上一个表单页面时携带提示消息
            ModelMap modelMap
    ) throws IOException {
        // 判断上传的文件是否为空
        if (headerPicture.isEmpty()) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        // 执行上传
        ResultEntity<String> uploadHeadPicFileToOss = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());
        String headPicFileToOssResult = uploadHeadPicFileToOss.getResult();
        if (ResultEntity.FAILED.equals(headPicFileToOssResult)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }
        String headerPicturePath = uploadHeadPicFileToOss.getData();
        projectVO.setHeaderPicturePath(headerPicturePath);
        // 创建用于存放详情图片的集合
        List<String> detailUploadPicFile = new ArrayList<>();
        // 判断详情图片集合是否为空
        if (detailPictureList == null || detailPictureList.size() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }
        for (MultipartFile detailPicFile : detailPictureList) {
            // 判断当前单个图片是否为空
            if (detailPicFile.isEmpty()) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            // 执行上传
            ResultEntity<String> detailPicResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicFile.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicFile.getOriginalFilename());
            String detailPicResult = detailPicResultEntity.getResult();
            if (ResultEntity.FAILED.equals(detailPicResult)) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
            String picResultEntityData = detailPicResultEntity.getData();
            detailUploadPicFile.add(picResultEntityData);

        }
        projectVO.setDetailPicturePathList(detailUploadPicFile);
        // 将projectVO存入Session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        // 这里要使用完整路径前往下一个收集回报信息的页面
        return "redirect:http://www.crowd.com/project/return/info/page";
    }

}
