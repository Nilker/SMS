package com.xy.it.xysms.controller;

import com.alibaba.fastjson.JSON;
import com.xy.it.xysms.repository.config.IRepositoryConfig;
import com.xy.it.xysms.service.ISmsService;
import com.xy.it.xysms.service.Result;
import com.xy.it.xysms.service.impl.Xyconfig;
import com.xy.it.xysms.util.CaptchaUtil;
import com.xy.it.xysms.util.VerificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Context;
import java.util.Locale;

@RestController
@RequestMapping("/xysms")
public class XySmsController {

    private ISmsService SMS_SERVICE;

    @Value("${sms.temContent}")
    private String temp;

    public XySmsController(IRepositoryConfig repositoryConfig) {
        Xyconfig config = new Xyconfig();
        config.setRepositoryConfig(repositoryConfig);
        SMS_SERVICE = VerificationUtil.createSmsService(config);
    }

    @RequestMapping("/send")
    public String xySmsSend(String phoneNo) {
        //生成的短信验证码
        String code = CaptchaUtil.create().getWord();
        //短信内容
        String text = String.format(Locale.getDefault(),temp,code);
        //业务标志
        String flag = "";

        Result<String> result = SMS_SERVICE.send(phoneNo, code, text, flag);

        return com.alibaba.fastjson.JSON.toJSONString(result);
    }

    @RequestMapping("/Verify")
    public String xySmsVerifying(String phoneNo, String smsCode, String imageCode) {
        //业务标志
        String flag = "";

        Result<Void> result = SMS_SERVICE.verify(phoneNo, smsCode, imageCode, flag);

        return JSON.toJSONString(result);
    }
}
