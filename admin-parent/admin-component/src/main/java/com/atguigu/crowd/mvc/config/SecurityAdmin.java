package com.atguigu.crowd.mvc.config;

import com.atguigu.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class SecurityAdmin extends User {
    private static final long serialVersionUID = 1L;
    private Admin originAdmin;

    public SecurityAdmin(Admin originAdmin, List<GrantedAuthority> authorities) {
        super(originAdmin.getUserName(), originAdmin.getUserPswd(), authorities);
        // 给originAdmin赋值
        this.originAdmin = originAdmin;
        // 将原始Admin中的密码进行擦除
        this.originAdmin.setUserPswd(null);
    }

    public Admin getOriginAdmin() {
        return originAdmin;
    }
}
