package com.xy.it.xysms.service.impl;


import com.xy.it.xysms.service.BaseConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * 云片配置实现类
 * Created by liamjung on 2018/1/22.
 */
@Getter
@Setter
public class YunpianConfig extends BaseConfig {

    /**
     * 云片提供的key
     */
    private String apiKey;
}
