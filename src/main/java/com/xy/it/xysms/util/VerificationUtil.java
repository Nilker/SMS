package com.xy.it.xysms.util;


import com.xy.it.xysms.service.BaseConfig;
import com.xy.it.xysms.service.ISmsService;
import com.xy.it.xysms.service.impl.XySmsService;
import com.xy.it.xysms.service.impl.Xyconfig;
import com.xy.it.xysms.service.impl.YunpianConfig;
import com.xy.it.xysms.service.impl.YunpianSmsService;

/**
 * 验证码工具类
 * Created by liamjung on 2018/1/22.
 */
public class VerificationUtil {

    /**
     * 创建云片短信服务
     *
     * @return
     */
    public static ISmsService createSmsService(BaseConfig config) {

        if (config instanceof YunpianConfig)
            return new YunpianSmsService((YunpianConfig) config);

        if (config instanceof Xyconfig)
            return new XySmsService((Xyconfig) config);
        //可扩展为其他短信运营上

        return null;
    }
}
