package be.busstop.global.exception;


import be.busstop.global.stringCode.ErrorCodeEnum;

public class InvalidConditionException extends IllegalArgumentException {

    ErrorCodeEnum errorCodeEnum;

    public InvalidConditionException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }

}