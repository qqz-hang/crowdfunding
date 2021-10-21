package com.atguigu.crowd.handler;

import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.service.api.ProjectService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProjectProviderHandler {
    @Resource
    private ProjectService projectService;

    @RequestMapping("save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,
                                             @RequestParam("memberId") Integer memberId) {
        try {
            // 调用本地service保存projectVO
            projectService.saveProjectVORemote(projectVO, memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
