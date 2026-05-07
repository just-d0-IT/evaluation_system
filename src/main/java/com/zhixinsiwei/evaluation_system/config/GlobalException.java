package com.zhixinsiwei.evaluation_system.config;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName GlobalException.java
 * @Description 异常处理 配置
 * @createTime 2023年12月24日 10:29:00
 */

import com.zhixinsiwei.evaluation_system.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                              HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return ResponseResult.fail(e.getMessage());
    }

    /**
     * 业务异常
     */
    //@ExceptionHandler(ServiceException.class)
    //public ResponseResult handleServiceException(ServiceException e) {
    //    log.error(e.getMessage(), e);
    //    Integer code = e.getCode();
    //    return ObjectUtil.isNotNull(code) ? ResponseResult.fail(code, e.getMessage()) : ResponseResult.fail(e.getMessage());
    //}

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return ResponseResult.fail(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return ResponseResult.fail(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    //@ExceptionHandler(BindException.class)
    //public ResponseResult handleBindException(BindException e) {
    //    log.error(e.getMessage(), e);
    //    String message = e.getAllErrors().get(0).getDefaultMessage();
    //    return ResponseResult.fail(message);
    //}

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseResult.fail(message);
    }

}
