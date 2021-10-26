package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressVO {
    private String receiveName;

    private String phoneNum;

    private String address;

    private Integer memberId;
}
