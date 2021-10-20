package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<>();

    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/member/auth/to/reg/page");
        PASS_RES_SET.add("/member/auth/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/do/member/login");
        PASS_RES_SET.add("/auth/do/member/reigster");
        PASS_RES_SET.add("/auth/member/send/short/message");
    }

    /**
     * 准备放行的静态资源
     */
    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    /**
     * 用于判断当前servletPath是否是静态资源
     *
     * @param servletPath
     * @return 返回false代表不是静态资源
     */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath) {
        if (servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        // 将servletPath以/分开
        String[] splitPath = servletPath.split("/");
        // 取出第一级的路径
        return STATIC_RES_SET.contains(splitPath[1]);
    }

}
