package com.shgx.mq.service.impl;

import com.shgx.mq.service.RedisSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description Redis消息保存
 * @auther guangxush
 * @create 2019/1/14
 */
@Service
public class RedisSaveServiceImpl implements RedisSaveService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 向通道发送消息的方法,数据保存在Redis中去
     * @param channel
     * @param message
     */
    @Override
    public Boolean sendChannelMess(String channel, String message) {
        try{
            stringRedisTemplate.convertAndSend(channel, message);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}