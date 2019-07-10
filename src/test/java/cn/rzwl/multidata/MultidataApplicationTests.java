package cn.rzwl.multidata;

import cn.rzwl.multidata.config.TestPerson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MultidataApplicationTests {

    @Autowired
    TestPerson testPerson;
    
    @Test
    public void contextLoads() {
    }

    @Test
    public void testPer(){
        String name = testPerson.getName();
        System.out.println(name);
    }
}
