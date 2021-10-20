package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberConfirmInfoVO implements Serializable {
    private static final long serialVersionUID = 2666020602154053979L;
    // 易付宝账号
    private String paynum;
    // 法人身份证号
    private String cardnum;
}
