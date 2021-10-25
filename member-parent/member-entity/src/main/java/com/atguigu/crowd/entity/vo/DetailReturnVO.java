package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnVO {
    private Integer returnId;
    // 支持金额
    private Integer supportMoney;
    // 是否限制单笔购买数量， 0 表示不限购， 1 表示限购
    private Integer signalPurchase;
    // 具体限购的数量
    private Integer purchase;
    //当前档位支持者数量
    private Integer supporterCount;
    // 运费， “0” 为包邮
    private Integer freight;
    // 回报内容介绍
    private String content;
    // 众筹结束后返还回报物品天数
    private Integer returnDate;
}
