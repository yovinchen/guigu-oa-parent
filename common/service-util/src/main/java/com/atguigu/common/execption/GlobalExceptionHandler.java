package com.atguigu.common.execption;

import com.atguigu.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * ClassName: GlobalExceptionHandler
 * Package: com.atguigu.common.handler
 *
 * @author yovinchen
 * @Create 2023/6/6 20:41
 * <p>
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("执行了特定异常处理");
    }

    @ExceptionHandler(GuiguException.class)
    @ResponseBody
    public Result error(GuiguException e) {
        e.printStackTrace();
        return Result.fail().message(e.getMsg()).code(e.getCode());
    }
}
