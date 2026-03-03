package com.tiv.inventory.transfer.exception;

import com.tiv.inventory.transfer.common.BusinessCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(BusinessCodeEnum businessCodeEnum) {
        super(businessCodeEnum.getMessage());
        this.code = businessCodeEnum.getCode();
    }

    public BusinessException(BusinessCodeEnum businessCodeEnum, String message) {
        super(message);
        this.code = businessCodeEnum.getCode();
    }

}
