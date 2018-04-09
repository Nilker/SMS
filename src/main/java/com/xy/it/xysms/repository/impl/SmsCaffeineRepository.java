package com.xy.it.xysms.repository.impl;


import com.xy.it.xysms.model.SmsModel;
import com.xy.it.xysms.repository.BaseCaffeineRepository;
import com.xy.it.xysms.repository.ISmsRepository;

/**
 * 短信caffeine仓储
 * Created by liamjung on 2018/1/23.
 */
public class SmsCaffeineRepository extends BaseCaffeineRepository implements ISmsRepository {

    public SmsCaffeineRepository(int smsModelExpiration) {

        super(smsModelExpiration);
    }

    @Override
    public void save(SmsModel model) {

        CACHE.put(model.id(), model);
    }

    @Override
    public void update(SmsModel model) {

        this.save(model);
    }

    @Override
    public SmsModel get(String phoneNo, String flag) {

        return CACHE.getIfPresent(SmsModel.id(phoneNo, flag));
    }

    @Override
    public void delete(String phoneNo, String flag) {

        //异步操作
        CACHE.invalidate(SmsModel.id(phoneNo, flag));

        //同步缓存
//        CACHE.cleanUp();
    }
}
