package com.atguigu.crowd.handler;

import com.atguigu.crowd.service.api.ProjectService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProjectProviderHandler {
    @Resource
    private ProjectService projectService;
}
