package com.xy.it.xysms.service.impl;

import com.alibaba.fastjson.JSON;
import com.xy.it.xysms.service.BaseSmsService;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 云片短信服务实现类
 * Created by liamjung on 2018/1/19.
 */
public class YunpianSmsService extends BaseSmsService<YunpianConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(YunpianSmsService.class);

    private final YunpianClient CLIENT;

    public YunpianSmsService(YunpianConfig config) {

        super(config);

        //初始化clnt,使用单例方式
        CLIENT = new YunpianClient(config.getApiKey()).init();

        //应用关闭时，执行
        //释放CLIENT
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                //为兼容java8之前的版本，采用非lambdas
                super.run();

                CLIENT.close();
            }
        });
    }

    @Override
    public void send(String phoneNo, String text) {

        //发送短信API
        Map<String, String> param = CLIENT.newParam(2);
        param.put(YunpianClient.MOBILE, phoneNo);
        param.put(YunpianClient.TEXT, text);
        Result<SmsSingleSend> result = CLIENT.sms().single_send(param);

        if (result.getThrowable() == null)
            LOGGER.info(JSON.toJSONString(result));
        else
            LOGGER.error(JSON.toJSONString(result));

        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*
    }
}
