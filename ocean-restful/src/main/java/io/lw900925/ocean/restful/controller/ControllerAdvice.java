package io.lw900925.ocean.restful.controller;

import com.google.common.collect.ImmutableMap;
import io.lw900925.ocean.support.spring.web.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> onException(Exception e, HttpServletRequest request) {
        LOGGER.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ImmutableMap.<String, Object> builder()
                        .put("timestamp", System.currentTimeMillis())
                        .put("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .put("message", e.getMessage())
                        .put("path", request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> onAppException(AppException e, HttpServletRequest request) {
        return ResponseEntity.status(e.getStatus()).body(
                ImmutableMap.<String, Object> builder()
                        .put("timestamp", System.currentTimeMillis())
                        .put("status", e.getStatus().value())
                        .put("error", e.getStatus().getReasonPhrase())
                        .put("message", messageSource.getMessage(e.getCode(), e.getArgs(), request.getLocale()))
                        .put("path", request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> onJsr303Exception(MethodArgumentNotValidException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ImmutableMap.<String, Object> builder()
                        .put("timestamp", System.currentTimeMillis())
                        .put("status", HttpStatus.BAD_REQUEST.value())
                        .put("error", HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .put("message", e.getMessage())
                        .put("path", request.getRequestURI())
                        .put("data", e.getBindingResult().getFieldErrors())
                        .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> onMissingParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ImmutableMap.<String, Object> builder()
                        .put("timestamp", System.currentTimeMillis())
                        .put("status", HttpStatus.BAD_REQUEST.value())
                        .put("error", HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .put("message", e.getMessage())
                        .put("path", request.getRequestURI())
                        .build());
    }
}
