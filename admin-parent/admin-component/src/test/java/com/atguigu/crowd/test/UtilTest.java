package com.atguigu.crowd.test;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;

public class UtilTest {
    @Test
    public void testMd5() {
        String source = "123123";
        String encryptMd5 = CrowdUtil.encryptMd5(source).toUpperCase();
        System.out.println(encryptMd5);
    }
}
