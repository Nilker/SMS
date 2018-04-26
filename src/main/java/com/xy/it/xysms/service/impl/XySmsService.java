package com.xy.it.xysms.service.impl;


import com.xy.it.xysms.service.BaseSmsService;
import com.xy.it.xysms.util.HttpUtil;
import com.xy.it.xysms.util.XySmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class XySmsService extends BaseSmsService<Xyconfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XySmsService.class);

    private final Xyconfig CLIENT;

    public XySmsService(Xyconfig config) {

        super(config);
        //初始化clnt,使用单例方式
        CLIENT = new Xyconfig();
    }


    /**
     * 发送通知
     *
     * @param phoneNo 手机号码
     * @param text    通知
     */
    @Override
    public void send(String phoneNo, String text) {
        //appId
        Integer appid = CONFIG.getAppid();
        //appKey
        String appKey = CONFIG.getAppKey();
        //ticket
        String ticket = String.valueOf(new Date().getTime());

        String passkey = XySmsUtil.GetNotePassKey(appid, appKey, ticket);

        String sendPostUrl="http://api.sys.xingyuanauto.com/sms/SendSMS";

        Map m1 = new HashMap();
        m1.put("appid",appid);
        m1.put("passkey",passkey);
        m1.put("notecount","1");
        m1.put("phonelist",phoneNo);
        m1.put("t",ticket);
        m1.put("noteContent",text);

        //发送请求
       String str= HttpUtil.doPost(sendPostUrl,m1);
    }
}
