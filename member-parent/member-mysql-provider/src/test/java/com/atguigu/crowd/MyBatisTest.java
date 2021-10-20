package com.atguigu.crowd;

import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.entity.po.MemberPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {
    @Resource
    private DataSource dataSource;
    @Resource
    private MemberPOMapper memberPOMapper;

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
