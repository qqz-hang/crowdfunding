package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.ProjectVO;

public interface ProjectService {
    void saveProjectVORemote(ProjectVO projectVO, Integer memberId);
}
