package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.service.api.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {
}
