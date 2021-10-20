package com.atguigu.crowd.mapper;


import com.atguigu.entity.Auth;
import com.atguigu.entity.AuthExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface AuthMapper {
    long countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);

    List<Integer> selectAssignedAuthIdByRoleId(Integer roleId);

    void insertNewRelationShip(@Param("roleId") Integer roleId, @Param("authIdList") List<Integer> authIdList);

    void deleteOldRelationShip(@Param("roleId") Integer roleId);

    List<String> selectAssignedAuthNameByAdminId(Integer adminId);
}