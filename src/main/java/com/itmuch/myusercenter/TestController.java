package com.itmuch.myusercenter;

import com.itmuch.myusercenter.dao.user.UserMapper;
import com.itmuch.myusercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author cgy
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final UserMapper userMapper;

    @GetMapping("/test")
    public User testInsert(){
        User user = new User();
        user.setAvatarUrl("xxx");
        user.setBonus(100);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
//        insert into 表名()values() 会把user所有的值列进去,但是主键如果没有设置的话会报错
//        this.userMapper.insert(user);
//        insert into 表名()values() 会把user set了值的这四个字段列进去，主键在generatorConfig.xml写了 <generatedKey column="id" sqlStatement="JDBC"/> 会自动生成
        this.userMapper.insertSelective(user);

        return user;
    }
}
