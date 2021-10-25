package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.service.api.MySQLRemoteService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class PortalHandler {
    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(Model model) {
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();
        String result = resultEntity.getResult();
        // 判断能否取到数据
        if (ResultEntity.SUCCESS.equals(result)) {
            List<PortalTypeVO> typeVOList = resultEntity.getData();
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, typeVOList);
        }
        return "portal";
    }
}
