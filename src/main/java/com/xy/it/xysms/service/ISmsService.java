package com.xy.it.xysms.service;

/**
 * 短信服务接口
 * Created by liamjung on 2018/1/19.
 */
public interface ISmsService {

    /**
     * 发送验证码
     *
     * @param phoneNo 手机号码
     * @param code    短信验证码
     * @param text    短信内容
     * @param flag    业务标志
     * @return 返回值中的data为null或验证码图片base64
     */
    Result<String> send(String phoneNo, String code, String text, String flag);

    /**
     * 发送通知
     *
     * @param phoneNo 手机号码
     * @param text    通知
     */
    void send(String phoneNo, String text);

    /**
     * 验证验证码
     *
     * @param phoneNo   手机号码
     * @param smsCode   短信验证码
     * @param imageCode 图片验证码
     * @param flag      业务标志
     * @return
     */
    Result<Void> verify(String phoneNo, String smsCode, String imageCode, String flag);

    /**
     * 刷新图片验证码
     *
     * @param phoneNo 手机号码
     * @param flag    业务标志
     * @return 返回值中的data为null或验证码图片base64
     */
    Result<String> refreshImageCode(String phoneNo, String flag);
}
