package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProjectVO {
    private Integer projectId;
    // 项目名称
    private String projectName;
    // 项目描述
    private String projectDesc;
    // 已关注人数
    private Integer followerCount;
    // 0-即将开始，1-众筹中，2-众筹成功，3-众筹失败
    private Integer status;
    // 计划筹集的金额
    private Integer money;
    // 筹集的金额
    private Integer supportMoney;
    // 完成的百分比
    private Integer percentage;
    // 项目发起时间
    private String deployDate;
    // 项目剩余时间
    private Integer lastDay;
    // 支持者的数量
    private Integer supporterCount;
    // 项目头图
    private String headerPicturePath;
    // 项目详情图
    private List<String> detailPicturePathList;
    // 项目详情的回报
    private List<DetailReturnVO> detailReturnVOList;

}
