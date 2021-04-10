package com.itmuch.myusercenter.service.user;

import com.itmuch.myusercenter.dao.bonus.BonusEventLogMapper;
import com.itmuch.myusercenter.dao.user.UserMapper;
import com.itmuch.myusercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.itmuch.myusercenter.domain.dto.user.UserLoginDTO;
import com.itmuch.myusercenter.domain.entity.bonus.BonusEventLog;
import com.itmuch.myusercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cgy
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;


    public User findById(Integer id){
        // select * from user where id = #{id}
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsgDTO message){
        // 1. 为用户加积分
        Integer userId = message.getUserId();
        Integer bonus = message.getBonus();
        User user = this.userMapper.selectByPrimaryKey(userId);

        user.setBonus(user.getBonus() + bonus);
        this.userMapper.updateByPrimaryKeySelective(user);

        // 2. 记录日志到bonus_event_log表里面
        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event("CONTRIBUTE")
                        .createTime(new Date())
                        .description("投稿加积分..")
                        .build()
        );

        log.info("积分添加完毕...");
    }

    public User login(UserLoginDTO loginDTO, String openId){
        User user = this.userMapper.selectOne(
                User.builder()
                        .wxId(openId)
                        .build()
        );
        if (user == null) {
            User userToSave = User.builder()
                    .wxId(openId)
                    .bonus(300)
                    .wxNickname(loginDTO.getWxNickname())
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .roles("user")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            this.userMapper.insertSelective(
                    userToSave
            );
            return userToSave;
        }
        return user;
    }
}
