package com.atguigu.crowd;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.DetailReturnVO;
import com.atguigu.crowd.entity.vo.PortalProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {
    @Resource
    private DataSource dataSource;
    @Resource
    private MemberPOMapper memberPOMapper;
    @Resource
    private ProjectPOMapper projectPOMapper;
    private Logger logger = LoggerFactory.getLogger(MyBatisTest.class);


    @Test
    public void loadDetailProjectData() {
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(3);
        logger.info(detailProjectVO.getProjectDesc()+"====");
        logger.info(detailProjectVO.getProjectName()+"====");
        logger.info(detailProjectVO.getProjectName()+"====");
        logger.info(detailProjectVO.getDeployDate()+"====");
        logger.info(detailProjectVO.getHeaderPicturePath()+"====");
        logger.info(detailProjectVO.getFollowerCount()+"====");
        logger.info(detailProjectVO.getLastDay()+"====");
        logger.info(detailProjectVO.getMoney()+"====");
        logger.info(detailProjectVO.getPercentage()+"====");
        List<String> detailPicturePathList = detailProjectVO.getDetailPicturePathList();
        for (String s : detailPicturePathList) {
            logger.info("=============detail"+s);
        }
        List<DetailReturnVO> detailReturnVOList = detailProjectVO.getDetailReturnVOList();
        for (DetailReturnVO detailReturnVO : detailReturnVOList) {
            logger.info(detailReturnVO.getContent()+"==0-0-0");
            logger.info(detailReturnVO.getReturnDate()+"==0-0-0");
            logger.info(detailReturnVO.getFreight()+"==0-0-0");
            logger.info(detailReturnVO.getPurchase()+"==0-0-0");
            logger.info(detailReturnVO.getSignalPurchase()+"==0-0-0");
            logger.info(detailReturnVO.getPurchase()+"==0-0-0");
        }
    }

    @Test
    public void testLoadTypeList() {
        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO portalTypeVO : portalTypeVOList) {
            String name = portalTypeVO.getName();
            String remark = portalTypeVO.getRemark();
            logger.info("name===" + name + "remark====" + remark);
            List<PortalProjectVO> projectVOList = portalTypeVO.getProjectVOList();
            for (PortalProjectVO portalProjectVO : projectVOList) {
                logger.info(portalProjectVO.toString());
                logger.info("headPic===========" + portalProjectVO.getHeaderPicturePath());
            }
        }
    }

    @Test
    public void testConn() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testMapper() {
        BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123123";
        String encodedPassword = cryptPasswordEncoder.encode(rawPassword);
        MemberPO memberPO = new MemberPO(null, "jack", encodedPassword, "杰克", "jack@qq.com", 1, 1, "杰克", "123123", 2);
        memberPOMapper.insert(memberPO);
    }
}
