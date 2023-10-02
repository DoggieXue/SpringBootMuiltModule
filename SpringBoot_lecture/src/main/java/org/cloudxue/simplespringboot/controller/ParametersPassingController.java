package org.cloudxue.simplespringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.cloudxue.simplespringboot.entity.Result;
import org.cloudxue.simplespringboot.entity.Student;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: SpringBoot中的参数传递与接收
 *
 * @Author: xuexiao
 * @Date: 2023年10月02日 15:59:40
 **/
@RestController
@RequestMapping("/api/param")
@Slf4j
public class ParametersPassingController {

    /**
     * 无注解普通传参，传递简单的字符串参数
     * 前端使用Post/Get方式，以Key-Value形式上送
     * 后端无需任何注解，方法形参与前端参数名相同即可接收
     * 方法中的形参paraName，必须与前端上送的参数名paramName一致
     * 可以接收Url后面拼接的查询参数，也可以接收Post方式的Body参数
     * @param paramName
     * @return
     */
    @RequestMapping("/simpleParameterPassing")
    public Result<String> getParamValue(String paramName) {
        log.info("无注解传参：参数名: {}, 参数值: {}", "paramName", paramName);
        return Result.success(paramName);
    }

    /**
     *
     * 前端使用Post/Get方式，以Key-Value形式上送
     * 后端使用@RequestParam注解，对应前端参数与方法形参的关系
     * @RequestParam 注解接收前端传参，value值必须跟前端上送的参数值相同，required属性默认true，若前端
     * 未上送value值指定的参数字段，则返回400，Bad Request，可以将required设为false，此时前端不送，则置为null
     * 可以接收Url后面拼接的查询参数，也可以接收Post方式的Body参数
     *
     * @param str
     * @return
     */
    @RequestMapping(value = "/parameterPassing")
    public Result getParamByAnnotation(@RequestParam(value = "paramName") String str) {
        log.info("@RequestParam注解接收参数，参数名：{}, 参数值：{}", "paramName", str);
        return Result.success(str);
    }

    /**
     * 无注解复杂传参
     * 可以接收Url后面拼接的查询参数，也可以接收Post方式的Body参数
     * 前端上送实体相关属性的json串，后端方法中使用实体形参接收
     * 前提是前端上送的json串各属性数量要小于等于实体中各变量的数量，实体类必须有getter/setter方法
     *
     * @param student
     */
    @RequestMapping(value = "/getEntity", method = RequestMethod.GET)
    public Result getEntityParam(Student student) {
        log.info("无注解复杂传参：{}", student.toString());
        return Result.success(student);
    }

    /**
     * 前端使用POST，在RequestBody中使用json串
     * 后端使用@RequestBody接收
     * @param student
     * @return
     */
    @RequestMapping("/postEntity")
    public Result getEntityParams(@RequestBody Student student) {
        log.info("POST方式上送json串：{}", student.toString());
        return Result.success(student);
    }

    /**
     *  前端使用GET方式，上送多个参数，后端无对应实体类，使用Map接收参数，需要使用@RequestParam注解
     * @param params
     * @return
     */
    @GetMapping("/getMapParams")
    public Result getMultiParams(@RequestParam Map params) {
        log.info("GET方式上送多个参数，Map接收： {}", params.toString());
        return Result.success(params);
    }

    /**
     * 前端使用POST方式，RequestBody里使用JSON串，后端无对应实体类，使用Map接收参数，需要使用@RequestBody注解
     * @param params
     * @return
     */
    @PostMapping("/postMapParams")
    public Result postMultiParams(@RequestBody Map params) {
        log.info("POST方式上送多个参数，Map接收： {}", params.toString());
        return Result.success(params);
    }

    /**
     * 前端使用Get方式，上送数组格式的数据（多个同名参数）
     * 后端使用@RequestParam注解接收，形参可用List<String>,也可以用String[]
     * @param nameList
     * @return
     */
    @GetMapping("/getArrayParams")
    public Result getArrayParams(@RequestParam(value = "name") List<String> nameList) {
        log.info("GET方式上送数组参数，@RequestParam接收： {}", nameList.toString());
        return Result.success(nameList);
    }

    /**
     *  前端POST方式，上送数组格式的数据
     *  后端使用@equestBody注解接收，形参可用List<String>,也可以用String[]
     * @param nameArray
     * @return
     */
    @PostMapping("/postArrayParams")
    public Result postArrayParams(@RequestBody String[] nameArray) {
        log.info("POST方式上送数组参数，@RequestBody接收： {}", nameArray.toString());
        return Result.success(nameArray);
    }

    /**
     *  接收前端的RequestHeader、Cookie中的参数
     * @param paramInHeader
     * @param paramInCookie
     * @return
     */
    @RequestMapping("/headerCookieParam")
    public Result headerCookieParams(@RequestHeader String paramInHeader,
                                     @CookieValue String paramInCookie) {
        log.info("获取Header和Cookie中的参数：paramInHeader = {}, paramInCookie = {}", paramInHeader, paramInCookie);
        return Result.success(paramInHeader + "_" + paramInCookie);
    }

    /**
     * 接收前端的RequestHeader、Cookie中的参数
     * @param request
     * @return
     */
    @RequestMapping("/requestParam")
    public Result requestParam(HttpServletRequest request) {
        String paramInHeader = request.getHeader("myHeader");
        String paramInCookie = "";
        for (Cookie cookie : request.getCookies()) {
            if ("myCookie".equals(cookie.getName())) {
                paramInCookie = cookie.getValue();
            }
        }
        log.info("获取Header和Cookie中的参数：paramInHeader = {}, paramInCookie = {}", paramInHeader, paramInCookie);
        return Result.success(paramInHeader + "_" + paramInCookie);
    }
}
