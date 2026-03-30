package com.upiicsa.ApiSIP.Exception;

import com.upiicsa.ApiSIP.Model.Enum.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String plusMessage) {
        super(errorCode.getDefaultMessage() + plusMessage);
        this.errorCode = errorCode;
    }
}
