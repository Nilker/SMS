package com.xy.it.xysms.repository.impl;

import com.alibaba.fastjson.JSON;

import com.xy.it.xysms.model.SmsModel;
import com.xy.it.xysms.repository.BaseRedisRepository;
import com.xy.it.xysms.repository.ISmsRepository;
import redis.clients.jedis.Jedis;

/**
 * 短信redis仓储
 * Created by liamjung on 2018/1/19.
 */
public class SmsRedisRepository extends BaseRedisRepository implements ISmsRepository {

    private final int SMS_MODEL_EXPIRATION;

    public SmsRedisRepository(int smsModelExpiration, String redisHost, String redisPass, int redisDb) {

        super(redisHost, redisPass, redisDb);

        SMS_MODEL_EXPIRATION = smsModelExpiration;
    }

    @Override
    public void save(SmsModel model) {

        Jedis jedis = null;

        try {
            jedis = super.getConnection();

            jedis.setex(model.id(), SMS_MODEL_EXPIRATION, JSON.toJSONString(model));
        } finally {

            super.closeConnection(jedis);
        }
    }

    @Override
    public void update(SmsModel model) {

        this.save(model);
    }

    @Override
    public SmsModel get(String phoneNo, String flag) {

        SmsModel model;

        Jedis jedis = null;

        try {
            jedis = super.getConnection();

            String json = jedis.get(SmsModel.id(phoneNo, flag));

            model = JSON.parseObject(json, SmsModel.class);
        } finally {

            super.closeConnection(jedis);
        }

        return model;
    }

    @Override
    public void delete(String phoneNo, String flag) {

        Jedis jedis = null;

        try {
            jedis = super.getConnection();

            jedis.del(SmsModel.id(phoneNo, flag));
        } finally {

            super.closeConnection(jedis);
        }
    }
}
