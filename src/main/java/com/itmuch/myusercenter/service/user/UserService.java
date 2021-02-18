package com.itmuch.myusercenter.service.user;

import com.itmuch.myusercenter.dao.user.UserMapper;
import com.itmuch.myusercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cgy
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    public User findById(Integer id){
        // select * from user where id = #{id}
        return this.userMapper.selectByPrimaryKey(id);
    }
}
