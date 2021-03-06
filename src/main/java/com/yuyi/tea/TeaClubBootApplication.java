package com.yuyi.tea;

import com.yuyi.tea.service.RedisService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@MapperScan("com.yuyi.tea.mapper")
@SpringBootApplication
@EnableCaching
//@ServletComponentScan(basePackages = "com.yuyi.tea.filter")
public class TeaClubBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeaClubBootApplication.class, args);
    }

}
