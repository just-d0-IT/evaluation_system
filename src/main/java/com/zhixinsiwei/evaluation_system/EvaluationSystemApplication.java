package com.zhixinsiwei.evaluation_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhixinsiwei.evaluation_system.mybatis_plus.repository")
public class EvaluationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluationSystemApplication.class, args);
    }

}
