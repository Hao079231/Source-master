package com.test.master.exception;

public class UnauthorizationException extends RuntimeException{
    private String code;
    public UnauthorizationException(String message){
        super(message);
    }

    public UnauthorizationException(String message, String code) {
        super(message);
        this.code = code;
    }
}
