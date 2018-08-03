package org.matrixstudio.ocean.restful.controller;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            EntityNotFoundException.class,
            EntityExistsException.class,
            HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class,
            RestException.class,
            Exception.class})
    public ResponseEntity<ServerError> onException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = e.getMessage();
        Object data = null;

        if (e instanceof MethodArgumentNotValidException) {
            // 处理JSR 303校验失败异常
            status = HttpStatus.BAD_REQUEST;
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            data = exception.getBindingResult().getFieldErrors();
        } else if (e instanceof EntityNotFoundException) {
            // 对象不存在异常
            status = HttpStatus.NOT_FOUND;
            message = messageSource.getMessage(EntityNotFoundException.class.getSimpleName(), null, request.getLocale());
        } else if (e instanceof EntityExistsException) {
            // 对象已存在异常
            status = HttpStatus.CONFLICT;
            message = messageSource.getMessage(EntityExistsException.class.getSimpleName(), null, request.getLocale());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
        } else if (e instanceof MissingServletRequestParameterException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof RestException) {
            // 自定义异常
            RestException exception = (RestException) e;
            status = exception.getStatus();
            message = messageSource.getMessage(exception.getCode(), exception.getArgs(), exception.getMessage(), request.getLocale());
        } else {
            LOGGER.error(message, e);
        }

        ServerError serverError = new ServerError();
        serverError.setTimestamp(System.currentTimeMillis());
        serverError.setStatus(status.value());
        serverError.setError(status.getReasonPhrase());
        serverError.setMessage(message);
        serverError.setPath(request.getRequestURL());
        serverError.setData(data);

        return ResponseEntity.status(status).body(serverError);
    }

    @Data
    private static class ServerError {
        private Long timestamp;
        private Integer status;
        private String error;
        private String message;
        private CharSequence path;
        private Object data;
    }
}
