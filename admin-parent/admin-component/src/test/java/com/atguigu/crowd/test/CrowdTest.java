package com.atguigu.crowd.test;


import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testTx() {
        Admin admin = new Admin(null, "zhang", "123", "张", "zhang@qq.com", null);
        adminMapper.insert(admin);

    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(null, "lisi", "123", "李四", "lisi@qq.com", null);
        int insert = adminMapper.insert(admin);
        System.out.println("受影响的行数" + insert);
    }

    @Test
    public void testLog() {
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        logger.debug("Debug Levels");
        logger.debug("Debug Levels");
        logger.debug("Debug Levels");
        logger.info("Info Levels");
        logger.info("Info Levels");
        logger.info("Info Levels");
        logger.warn("Warn Levels");
        logger.warn("Warn Levels");
        logger.warn("Warn Levels");
        logger.error("Error Levels");
        logger.error("Error Levels");
        logger.error("Error Levels");
    }

    @Test
    public void testInsetAdmin() {
        for (int i = 0; i < 244; i++) {
            adminMapper.insert(new Admin(null, "loginAcct" + i, "123456", "用户" + i, "loginAcct" + i + "@qq.com", null));
        }
    }

    @Test
    public void testRoleSave() {
        for (int i = 0; i < 235; i++) {
            roleMapper.insert(new Role(null, "role" + i));
        }
    }
}
