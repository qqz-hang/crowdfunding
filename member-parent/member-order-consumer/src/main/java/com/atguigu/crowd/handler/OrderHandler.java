package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.service.api.MySQLRemoteService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class OrderHandler {
    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount) {
        return null;

    }

    @RequestMapping("/confirm/return/info/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("returnId") Integer returnId,
            HttpSession session) {
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(returnId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            OrderProjectVO resultEntityData = resultEntity.getData();
            //  为了能够在后续操作中保持 orderProjectVO 数据，存入 Session 域
            session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, resultEntityData);
        }
        return "confirm-return";
    }
}
