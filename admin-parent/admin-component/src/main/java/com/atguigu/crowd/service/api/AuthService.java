package com.atguigu.crowd.service.api;

import com.atguigu.entity.Auth;

import java.util.List;
import java.util.Map;

public interface AuthService {
    List<Auth> getAllAuth();

    List<Integer> getAssignedAuthIdByRoleId(Integer roleId);


    void saveRoleAuthRelationShip(Integer roleId, List<Integer> authIdList);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
