package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.entity.Auth;
import com.atguigu.entity.AuthExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAllAuth() {
        return authMapper.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAssignedAuthIdByRoleId(Integer roleId) {
        return authMapper.selectAssignedAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationShip(Integer roleId, List<Integer> authIdList) {
        // 删除旧数据
        authMapper.deleteOldRelationShip(roleId);
        if (authIdList != null && authIdList.size() > 0) {
            authMapper.insertNewRelationShip(roleId, authIdList);
        }
    }

    @Override
    public List<String> getAssignedAuthNameByAdminId(Integer adminId) {

        return authMapper.selectAssignedAuthNameByAdminId(adminId);
    }
}
