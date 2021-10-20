package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class CrowdUserDetailService implements UserDetailsService {
    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;
    @Resource
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询到Admin
        Admin admin = adminService.getAdminByLoginAcct(username);
        // 用admin查询到adminId
        Integer adminId = admin.getId();
        // 用adminId查询到role
        List<Role> roleList = roleService.getAssignedRole(adminId);
        // 用adminId查询到已分配的权限
        List<String> nameList = authService.getAssignedAuthNameByAdminId(adminId);
        // 创建集合用来存储GrantedAuthority
        List<GrantedAuthority> authorized = new ArrayList<>();
        // 遍历roleList 将role加载到authorized中
        for (Role role : roleList) {
            String roleName = "ROLE_" + role.getName();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authorized.add(simpleGrantedAuthority);
        }
        // 遍历name存入权限信息
        for (String name : nameList) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(name);
            authorized.add(simpleGrantedAuthority);
        }
        // 封装securityAdmin对象
        return new SecurityAdmin(admin, authorized);
    }
}
