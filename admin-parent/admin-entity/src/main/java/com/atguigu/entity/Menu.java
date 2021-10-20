package com.atguigu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private Integer id;

    private Integer pid;

    private String name;

    private String url;

    private String icon;
    // 初始化 防止空指针
    private List<Menu> children = new ArrayList<>();
    private Boolean open = true;
}