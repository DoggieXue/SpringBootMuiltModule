package org.cloudxue.simplespringboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @ClassName WebConfiguration
 * @Description: 解决统一包装处理响应消息时遇到的 类型转换异常 方案二
 *              xxx.Result cannot be cast to java.lang.String问题
 *              注意：此方案需要再Controller中@RequestMapping/@GetMapping中，通过produces参数指定响应的ContentType
 *
 * @Author: Doggie
 * @Date: 2023年08月09日 16:34:12
 * @Version 1.0
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * 交换MappingJackson2HttpMessageConverter与第一位元素
     * 让返回值类型为String的接口可以正常返回包装结果
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter)converters.get(i);
                converters.set(i, converters.get(0));
                converters.set(0, converter);
                break;
            }
        }
    }

    /**
     * 在集合中第一位添加MappingJackson2HttpMessageConverter
     * @param converters
     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(0, new MappingJackson2HttpMessageConverter());
//    }
}
