package com.atguigu.crowd.handler;

import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * create by: zr
 * description: TODO
 * create time: 2021/10/21 10:27
 * 接受表单数据
 *
 * @Param: null
 * @return
 */
@Controller
public class ProjectConsumerHandler {
    @Resource
    private OSSProperties ossProperties;

    @RequestMapping("/create/project/information")
    public String saveProjectBasicInfo(
            // 接受除了上传图片之外的普通数据
            ProjectVO projectVO,
            // 接受上传的头图
            MultipartFile headerPicture,
            // 接受上传的详情图片
            List<MultipartFile> detailPictureList,
            // 将收集了一部分数据的ProjectVO存入Session域中
            HttpSession session,
            // 用来在当前操作失败后返回上一个表单页面时携带提示消息
            ModelMap modelMap
    ) throws IOException {
        // 判断上传的文件是否为空
        if (headerPicture.isEmpty()) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        // 执行上传
        ResultEntity<String> uploadHeadPicFileToOss = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());
        String headPicFileToOssResult = uploadHeadPicFileToOss.getResult();
        if (ResultEntity.FAILED.equals(headPicFileToOssResult)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }
        String headerPicturePath = uploadHeadPicFileToOss.getData();
        projectVO.setHeaderPicturePath(headerPicturePath);
        // 创建用于存放详情图片的集合
        List<String> detailUploadPicFile = new ArrayList<>();
        // 判断详情图片集合是否为空
        if (detailPictureList == null || detailPictureList.size() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }
        for (MultipartFile detailPicFile : detailPictureList) {
            // 判断当前单个图片是否为空
            if (detailPicFile.isEmpty()) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            // 执行上传
            ResultEntity<String> detailPicResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicFile.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicFile.getOriginalFilename());
            String detailPicResult = detailPicResultEntity.getResult();
            if (ResultEntity.FAILED.equals(detailPicResult)) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
            String picResultEntityData = detailPicResultEntity.getData();
            detailUploadPicFile.add(picResultEntityData);

        }
        projectVO.setDetailPicturePathList(detailUploadPicFile);
        // 将projectVO存入Session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        // 这里要使用完整路径前往下一个收集回报信息的页面
        return "redirect:http://www.crowd.com/project/return/info/page";
    }
}
