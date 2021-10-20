package com.atguigu.crowd.exception;

/**
 * 登录失败异常
 */
public class LoginFailedException extends RuntimeException {
    private static final long serialVersionUID = -6840297962419767548L;

    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
