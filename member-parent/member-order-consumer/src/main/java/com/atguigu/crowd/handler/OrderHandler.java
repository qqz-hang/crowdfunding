package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.service.api.MySQLRemoteService;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {
    @Resource
    private MySQLRemoteService mySQLRemoteService;
    private Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {
        // 地址信息的保存
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);
        logger.debug("保存地址信息的结果：" + resultEntity.getResult());
        // 从Session域中取出orderProjectVO
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        // 从orderProjectVO取出ReturnCount
        Integer returnCount = orderProjectVO.getReturnCount();
        // 重定向到confirm/order/{returnCount}
        return "redirect:http://www.crowd.com/order//confirm/order/" + returnCount;
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount,
                                       HttpSession session) {
        // 将接收到的回报数量合并到Session域中
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT);
        orderProjectVO.setReturnCount(returnCount);
        // 再重新放到Session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_ORDER_PROJECT, orderProjectVO);
        // 根据用户id查询目前的收货地址数据
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberLoginVOId = memberLoginVO.getId();
        ResultEntity<List<AddressVO>> addressResult = mySQLRemoteService.getAddressVORemote(memberLoginVOId);
        if (ResultEntity.SUCCESS.equals(addressResult.getResult())) {
            List<AddressVO> addressVOList = addressResult.getData();
            session.setAttribute("addressVOList", addressVOList);
        }
        return "confirm-order";

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
