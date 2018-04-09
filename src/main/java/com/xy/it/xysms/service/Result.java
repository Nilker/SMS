package com.xy.it.xysms.service;


import com.xy.it.xysms.enumeration.ErrorEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 结果
 * Created by liamjung on 2018/1/19.
 */
@Getter
@Setter
@Builder
public class Result<D> {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 数据
     */
    private D data;

    /**
     * 构造成功结果
     *
     * @return
     */
    static Result success() {
        return success(null);
    }

    /**
     * 构造成功结果
     *
     * @param data
     * @return
     */
    static Result success(Object data) {
        return Result.builder()
                .success(true)
                .data(data)
                .build();
    }

    /**
     * 构造错误结果
     *
     * @param errorEnum
     * @return
     */
    static Result error(ErrorEnum errorEnum) {
        return Result.builder()
                .success(false)
                .errorCode(errorEnum.code())
                .errorMsg(errorEnum.msg())
                .build();
    }
}
