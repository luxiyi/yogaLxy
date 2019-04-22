package com.woniu.yago;

import com.woniu.yago.dao.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YagoApplicationTests {
    @Autowired
    private UserMapper mapper;
    @Test
    public void contextLoads() {
//        System.out.println(mapper.find(("17554264670"),0));
//        System.out.println(mapper.updateCode("2","17554264670"));
    }

}
