package com.atguigu.crowd.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPO {
    private Integer id;

    private String projectName;

    private String projectDescription;

    private Integer money;

    private Integer day;

    private Integer status;

    private String deploydate;

    private Integer supportmoney;

    private Integer supporter;

    private Integer completion;

    private Integer memberid;

    private String createdate;

    private Integer follower;

    private String headerPicturePath;
}