package com.shgx.mq.service.impl;

import com.shgx.mq.service.ReceiveService;
import com.shgx.mq.service.RedisSaveService;
import com.shgx.mq.service.RedisSendDataScheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @auther guangxush
 * @create 2019/1/14
 */
@Service
public class RedisSendDataScheduledImpl implements RedisSendDataScheduled {

    @Autowired
    RedisSaveService redisSaveService;

    @Autowired
    ReceiveService receiveService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${redis.list.key}")
    private String redisKey;

    /**
     * 收到通道的消息之后执行的方法
     * @param message
     */
    @Override
    public void receiveMessage(String message) {
        System.out.println("receive the message is: "+message);
        receiveService.process(message);
    }

    /**
     * 定时将Rerdis中的数据弹出，发送给数据库 (2分钟)
     */
    @Override
    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void popListMessage(){
        while(stringRedisTemplate.opsForList().size(redisKey)!=0){
            String mess = stringRedisTemplate.opsForList().leftPop(redisKey);
            receiveService.process(mess);
        }
    }

}
