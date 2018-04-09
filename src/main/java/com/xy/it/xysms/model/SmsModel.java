package com.xy.it.xysms.model;

import lombok.*;

/**
 * 短信模型
 * Created by liamjung on 2018/1/19.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsModel {

    /**
     * 手机号码
     */
    private String phoneNo;
    /**
     * 短信验证码
     */
    private String smsCode;
    /**
     * 短信验证码过期时间
     */
    private Long smsCodeExpiration;
    /**
     * 短信验证码重发时间
     */
    private Long smsCodeResendingTime;
    /**
     * 图片验证码
     */
    private String imageCode;
    /**
     * 图片验证码过期时间
     */
    private Long imageCodeExpiration;
    /**
     * 已发次数
     */
    private int sendingCount = 0;
    /**
     * 已验次数
     */
    private int verifyingCount = 0;
    /**
     * 业务标志
     */
    private String flag;

    public String id() {

        return this.flag + "::" + this.phoneNo;
    }

    public static String id(String phoneNo, String flag) {

        return flag + "::" + phoneNo;
    }
}
