package cn.rzwl.multidata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan(basePackages = "cn.rzwl.multidata.config")
public class MultidataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultidataApplication.class, args);
    }

}
