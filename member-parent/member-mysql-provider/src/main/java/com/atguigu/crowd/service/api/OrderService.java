package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;

import java.util.List;

public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberLoginVOId);

    void saveAddress(AddressVO addressVO);
}
