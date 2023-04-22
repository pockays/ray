package com.ray;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author liuris
 * @create 2023-03-19-15:22
 */
@MapperScan("com.ray.mapper")
@SpringBootApplication
@EnableSwagger2
public class RayBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(RayBlogApplication.class,args);
    }
}
