package org.cloud.xue.simplespringboot.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloud.xue.simplespringboot.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @ClassName ResponseAdvice
 * @Description: 利用ResponseBodyAdvice，对响应消息进行统一处理
 * @Author: Doggie
 * @Date: 2023年08月09日 15:33:16
 * @Version 1.0
 **/

@RestControllerAdvice(basePackages = "org.cloudxue.simplespringboot")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    public ObjectMapper objectMapper;
    /**
     * 判断是否要交给beforeBodyWrite方法执行
     * @param methodParameter
     * @param aClass
     * @return true:需要；false:不需要
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //如果不需要进行封装，可以添加一些校验
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //可以提供一些灵活校验，比如body已经被包装了，就不需要再包装
        if (body instanceof Result) {
            return body;
        }

        //方案一：解决 xxx.Result cannot be cast to java.lang.String问题
//        if (body instanceof String) {
//            try {
//                return this.objectMapper.writeValueAsString(Result.success(body));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }

        //统一包装处理返回结果
        return Result.success(body);
    }
}
