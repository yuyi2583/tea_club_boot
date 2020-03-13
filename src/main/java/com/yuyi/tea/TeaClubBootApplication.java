package com.yuyi.tea;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.yuyi.tea.mapper")
@SpringBootApplication
@EnableCaching
public class TeaClubBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeaClubBootApplication.class, args);
    }

}
