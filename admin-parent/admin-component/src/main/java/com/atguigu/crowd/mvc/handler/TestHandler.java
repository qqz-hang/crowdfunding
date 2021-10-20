package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.entity.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TestHandler {
    @Resource
    private AdminService adminService;


    @RequestMapping("/send/array.html")
    @ResponseBody
    public String testReceiveArray(@RequestBody List<Integer> array) {
        Logger logger = LoggerFactory.getLogger(TestHandler.class);
        for (Integer integer : array) {
            logger.info("number====" + integer);
        }
        return "success";
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap) {
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList", adminList);
        System.out.println(10 / 0);
        return "target";
    }
}
