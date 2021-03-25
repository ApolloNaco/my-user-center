package com.itmuch.myusercenter;

import com.itmuch.myusercenter.rocketmq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;

// 扫描mybatis哪些包里面的接口
@MapperScan("com.itmuch.myusercenter.dao")
@SpringBootApplication
@EnableBinding({Sink.class, MySink.class})
public class MyUserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyUserCenterApplication.class, args);
    }

}
