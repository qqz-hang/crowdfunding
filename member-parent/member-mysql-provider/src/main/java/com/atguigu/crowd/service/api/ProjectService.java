package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProjectVORemote(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalTypeVO();
}
