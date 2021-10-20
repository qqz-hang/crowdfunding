package com.atguigu.crowd.exception;

public class AdminEditFailedException extends RuntimeException{
    public AdminEditFailedException() {
        super();
    }

    public AdminEditFailedException(String message) {
        super(message);
    }
}
