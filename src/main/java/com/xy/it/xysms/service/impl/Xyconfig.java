package com.xy.it.xysms.service.impl;

import com.xy.it.xysms.service.BaseConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
public class Xyconfig extends BaseConfig {
    private Integer appid;

    private String appKey;

}
