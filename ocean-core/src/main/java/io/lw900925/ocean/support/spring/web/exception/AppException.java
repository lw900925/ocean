package io.lw900925.ocean.support.spring.web.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

    private HttpStatus status;
    private String code;
    private Object[] args;

    public AppException(HttpStatus status, String code, Object... args) {
        this.status = status;
        this.code = code;
        this.args = args;
    }

    public AppException(String code, Object... args) {
        this.status = HttpStatus.BAD_REQUEST;
        this.code = code;
        this.args = args;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
