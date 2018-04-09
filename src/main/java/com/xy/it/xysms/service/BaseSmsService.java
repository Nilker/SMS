package com.xy.it.xysms.service;

import com.github.bingoohuang.patchca.service.Captcha;

import com.xy.it.xysms.enumeration.ErrorEnum;
import com.xy.it.xysms.model.SmsModel;
import com.xy.it.xysms.repository.ISmsRepository;
import com.xy.it.xysms.repository.config.impl.RedisRepositoryConfig;
import com.xy.it.xysms.repository.impl.SmsCaffeineRepository;
import com.xy.it.xysms.repository.impl.SmsRedisRepository;
import com.xy.it.xysms.util.CaptchaUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 短信服务抽象类
 * Created by liamjung on 2018/1/19.
 */
public abstract class BaseSmsService<C extends BaseConfig> implements ISmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseSmsService.class);

    private final C CONFIG;

    private final ISmsRepository SMS_REPOSITORY;

    protected BaseSmsService(C config) {

        CONFIG = config;

        if (config.getRepositoryConfig() == null) {

            SMS_REPOSITORY = new SmsCaffeineRepository(config.getSmsModelExpiration());
        } else if (config.getRepositoryConfig() instanceof RedisRepositoryConfig) {

            RedisRepositoryConfig repositoryConfig = (RedisRepositoryConfig) config.getRepositoryConfig();

            SMS_REPOSITORY = new SmsRedisRepository(config.getSmsModelExpiration(), repositoryConfig.getHost(), repositoryConfig.getPass(), repositoryConfig.getDb());
        } else {
            //可扩展为其他仓储机制
            throw new IllegalArgumentException("Illegal repository config");
        }
    }

    @Override
    public final Result<String> send(String phoneNo, String code, String text, String flag) {

        try {
            SmsModel model = SMS_REPOSITORY.get(phoneNo, flag);

            long now = System.currentTimeMillis();

            if (model == null) {
                //有效期内未创建

                model = SmsModel.builder()
                        .phoneNo(phoneNo)
                        .smsCode(code)
                        .smsCodeExpiration(this.smsCodeExpiration())
                        .smsCodeResendingTime(this.smsCodeResendingTime())
//                    .imageCode(null)
//                    .imageCodeExpiration(null)
                        .sendingCount(1)
                        .flag(flag)
                        .build();

                //发送验证码
                this.send(phoneNo, text);

                //新增
                SMS_REPOSITORY.save(model);

                return Result.success();
            }

            //有效期内已创建

            if (now <= model.getSmsCodeResendingTime()) {
                //未到重发时间

                return Result.error(ErrorEnum.SMS_CODE_RESENDING_FAIL);
            }

            if (model.getSendingCount() >= CONFIG.getSmsMaxSendingCount()) {
                //限制次数

                return Result.error(ErrorEnum.SMS_CODE_EXCEED_SENDING_MAXIMUM);
            }

            String imageBase64 = null;

            model.setSmsCode(code);
            model.setSmsCodeExpiration(this.smsCodeExpiration());
            model.setSmsCodeResendingTime(this.smsCodeResendingTime());
            model.setVerifyingCount(0);

            if (model.getSendingCount() >= CONFIG.getImageCodeDisplayCount()) {
                //表示已发送短信验证码次数大于等于imageCodeDisplayCount时，需输入图片验证码
                Captcha captcha = CaptchaUtil.create();

                model.setImageCode(captcha.getChallenge());
                model.setImageCodeExpiration(this.imageCodeExpiration());

                imageBase64 = this.base64(captcha);
            }

            //发送验证码
            this.send(phoneNo, text);

            //增加发送次数
            model.setSendingCount(model.getSendingCount() + 1);

            SMS_REPOSITORY.update(model);

            return Result.success(imageBase64);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return Result.error(ErrorEnum.UNKNOWN);
        }
    }

    @Override
    public final Result<Void> verify(String phoneNo, String smsCode, String imageCode, String flag) {

        try {
            SmsModel model = SMS_REPOSITORY.get(phoneNo, flag);

            if (model == null)
                return Result.error(ErrorEnum.SMS_CODE_NOT_EXISTED);

            long now = System.currentTimeMillis();

            //超过短信验证码验证次数限制时，立即过期，防止暴力验证
            if (model.getVerifyingCount() >= CONFIG.getSmsCodeMaxVerifyingCount()) {

                //设置过期
                model.setSmsCodeExpiration(now);

                SMS_REPOSITORY.update(model);

                return Result.error(ErrorEnum.SMS_CODE_EXCEED_VERIFYING_MAXIMUM);
            }

            //增加验证次数
            model.setVerifyingCount(model.getVerifyingCount() + 1);

            SMS_REPOSITORY.update(model);

            //验证短信验证码
            if (!model.getSmsCode().equals(smsCode))
                return Result.error(ErrorEnum.SMS_CODE_INCORRECT);

            //验证短信验证码过期时间
            if (now > model.getSmsCodeExpiration())
                return Result.error(ErrorEnum.SMS_CODE_EXPIRED);

            if (model.getImageCode() != null) {
                //验证图片验证码
                if (!model.getImageCode().equals(imageCode))
                    return Result.error(ErrorEnum.IMAGE_CODE_INCORRECT);

                //验证图片验证码过期时间
                if (now > model.getImageCodeExpiration())
                    return Result.error(ErrorEnum.IMAGE_CODE_EXPIRED);
            }

            SMS_REPOSITORY.delete(phoneNo, flag);

            return Result.success();

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return Result.error(ErrorEnum.UNKNOWN);
        }
    }

    @Override
    public Result<String> refreshImageCode(String phoneNo, String flag) {

        try {

            SmsModel model = SMS_REPOSITORY.get(phoneNo, flag);

            if (model == null) {

                LOGGER.warn("因未发送短信验证码或短信验证码过期，刷新图片验证码失败（phoneNo: {}; flag: {}）", phoneNo, flag);

                return Result.success();
            }

            String imageBase64 = null;

            if (model.getSendingCount() >= CONFIG.getImageCodeDisplayCount()) {
                //表示已发送短信验证码次数大于等于imageCodeDisplayCount时，需输入图片验证码
                Captcha captcha = CaptchaUtil.create();

                model.setImageCode(captcha.getChallenge());
                model.setImageCodeExpiration(this.imageCodeExpiration());

                imageBase64 = this.base64(captcha);

                SMS_REPOSITORY.update(model);
            }

            return Result.success(imageBase64);

        } catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            return Result.error(ErrorEnum.UNKNOWN);
        }
    }

    /**
     * 返回短信验证码重发时间
     *
     * @return
     */
    private long smsCodeResendingTime() {
        return System.currentTimeMillis() + CONFIG.getSmsCodeResendingInterval();
    }

    /**
     * 返回短信验证码过期时间
     *
     * @return
     */
    private long smsCodeExpiration() {
        return System.currentTimeMillis() + CONFIG.getSmsCodeExpiration();
    }

    /**
     * 返回图片验证码过期时间
     *
     * @return
     */
    private long imageCodeExpiration() {
        return System.currentTimeMillis() + CONFIG.getImageCodeExpiration();
    }

    /**
     * 返回图片验证码base64
     *
     * @param captcha
     * @return
     */
    private String base64(Captcha captcha) {

        String base64 = null;

        BufferedImage image = captcha.getImage();

        ByteArrayOutputStream bos = null;

        try {
            bos = new ByteArrayOutputStream();

            ImageIO.write(image, "png", bos);
            byte[] imageBytes = bos.toByteArray();

            base64 = Base64.encodeBase64String(imageBytes);
        } catch (IOException ioe) {
            //打印异常，并忽略
            LOGGER.error(ioe.getMessage(), ioe);
        } finally {

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ioe) {
                    //打印异常，并忽略
                    LOGGER.error(ioe.getMessage(), ioe);
                }
            }
        }

        base64 = "data:image/png;base64," + base64;

        return base64;
    }
}
