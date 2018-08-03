package org.matrixstudio.ocean.restful.controller;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class RestException extends RuntimeException {
    private HttpStatus status;
    private String code;
    private Object[] args;

    public RestException(HttpStatus status, String code, String message, Object... args) {
        super(message);
        this.status = status;
        this.code = code;
        this.args = args;
    }
}
