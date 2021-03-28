package com.itmuch.myusercenter.rocketmq;

import com.alibaba.fastjson.JSON;
import com.itmuch.myusercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.itmuch.myusercenter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusStreamConsumer {

    private final UserService userService;

    @StreamListener(Sink.INPUT)
    public void receive(String message){
        UserAddBonusMsgDTO userAddBonusMsgDTO = JSON.parseObject(message, UserAddBonusMsgDTO.class);
        this.userService.addBonus(userAddBonusMsgDTO);
    }
}
