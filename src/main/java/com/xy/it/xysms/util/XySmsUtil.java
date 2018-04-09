package com.xy.it.xysms.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * xy短信接口帮助类
 * add by lhl
 * 2018年4月4日16:11:46
 */
public class XySmsUtil {
    //获取key
    public static String GetNotePassKey(Integer appId, String appKey, String ticket)  {
        String Str = ("@@" + appId.toString() + appKey + ticket + "__").toUpperCase();

        String encodeStr = DigestUtils.md5Hex(Str).toUpperCase();
        System.out.println("MD5加密后的字符串为:encodeStr=" + encodeStr);
        return encodeStr;
    }


}
