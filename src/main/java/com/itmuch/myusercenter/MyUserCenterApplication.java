package com.itmuch.myusercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

// 扫描mybatis哪些包里面的接口
@MapperScan("com.itmuch.myusercenter.dao")
@SpringBootApplication
public class MyUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyUserCenterApplication.class, args);
    }

}
