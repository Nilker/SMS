package com.xy.it.xysms.service;


import com.xy.it.xysms.repository.config.IRepositoryConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * 配置抽象类
 * Created by liamjung on 2018/1/19.
 */
@Getter
@Setter
public abstract class BaseConfig {

    /**
     * 短信模型过期时间
     * 必须大于短信验证码过期时间和短信验证码过期时间
     * 单位：秒
     * 默认值：15分钟
     */
    private int smsModelExpiration = 15 * 60;

    /**
     * 发送短信最大次数，必须大于1
     * 表示短信模型过期时间内，已发送短信验证码次数大于等于smsMaxSendingCount时，不再发送短信，提示错误
     * 默认值：10次
     */
    private int smsMaxSendingCount = 10;

    /**
     * 短信验证码过期时间
     * 单位：毫秒
     * 默认值：10分钟
     */
    private long smsCodeExpiration = 10 * 60 * 1000;

    /**
     * 验证短信验证码最大次数，必须大于1
     * 表示短信模型过期时间内，已验证短信验证码次数大于等于smsCodeMaxVerifyingCount时，立即过期，提示错误
     * 默认值：3次
     */
    private int smsCodeMaxVerifyingCount = 3;

    /**
     * 短信验证码重发间隔
     * 单位：毫秒
     * 默认值：1分钟
     */
    private long smsCodeResendingInterval = 10 * 1000; //10miao

    /**
     * 图片验证码过期时间
     * 单位：毫秒
     * 默认值：5分钟
     */
    private long imageCodeExpiration = 5 * 60 * 1000;

    /**
     * 图片验证码显示计数
     * 表示已发送短信验证码次数大于等于imageCodeDisplayCount时，需输入图片验证码
     * 默认值：2次
     */
    private int imageCodeDisplayCount = 20;

    /**
     * 仓储配置
     * 默认值：null，意为本地仓储（缓存）
     */
    private IRepositoryConfig repositoryConfig = null;
}
