package com.hust.baseweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableSwagger2WebMvc
@EnableScheduling
public class BasewebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasewebApplication.class, args);
    }
}
