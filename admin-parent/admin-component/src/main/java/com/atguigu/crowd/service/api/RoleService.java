package com.atguigu.crowd.service.api;

import com.atguigu.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {
    PageInfo<Role> getPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void saveRole(Role role);

    void updateRole(Role role);

    /**
     * 批量删除
     *
     * @param roleIdList
     */
    void removeRole(List<Integer> roleIdList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);
}
