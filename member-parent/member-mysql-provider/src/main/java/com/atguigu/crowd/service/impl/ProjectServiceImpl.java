package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberConfirmInfoPO;
import com.atguigu.crowd.entity.po.MemberLaunchInfoPO;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {
    @Resource
    private ReturnPOMapper returnPOMapper;
    @Resource
    private ProjectPOMapper projectPOMapper;
    @Resource
    private ProjectItemPicPOMapper projectItemPicPOMapper;
    @Resource
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
    @Resource
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProjectVORemote(ProjectVO projectVO, Integer memberId) {
        // 保存ProjectPO
        ProjectPO projectPO = new ProjectPO();
        // 将projectVO的属性复制到projectPO
        BeanUtils.copyProperties(projectVO, projectPO);
        // 保存memberId
        projectPO.setMemberid(memberId);
        // 生成创建时间存入
        String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createDate);
        // 生成状态 设置为0 表示即将开始
        projectPO.setStatus(0);
        //  选择性保存projectPO 需要到projectPOMapper进行设置
        projectPOMapper.insertSelective(projectPO);
        // 获取自增Id
        Integer projectId = projectPO.getId();
        // 保存项目 分类的关联关系信息
        // 从projectVO中获取typeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationShip(typeIdList, projectId);
        // 保存项目 标签的关联关系信息
        // 从projectVO中获取tagIdList
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationShip(tagIdList, projectId);
        // 保存项目中详情图片路径信息
        // 从projectVO中获取detailPicturePathList
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);
        // 保存项目发起人信息
        MemberLaunchInfoVO memberLaunchInfoVO = projectVO.getMemberLaunchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLaunchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);
        // 保存项目回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        // 将returnVOList转换为ReturnPOList
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOList.add(returnPO);
        }
        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);
        // 保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }
}
