package cn.rzwl.multidata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author : championjing
 * @ClassName: TestPerson
 * @Description:
 * @Date: 7/10/2019 5:50 PM
 */
@Component
@ConfigurationProperties(prefix = "person")
@Data
public class TestPerson {
    private Long id;
    private int age;
    private String name;
}
