package com.example.common.security.handler;

import com.example.common.core.exception.LqlCommonException;
import com.example.common.core.web.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * 一个http请求，在到达Controller前，会对该请求的请求信息与目标控制器信息做一系列校验。这里简单说一下：
 *
 * NoHandlerFoundException：首先根据请求Url查找有没有对应的控制器，若没有则会抛该异常，也就是大家非常熟悉的404异常；
 *
 * HttpRequestMethodNotSupportedException：若匹配到了（匹配结果是一个列表，不同的是http方法不同，如：Get、Post等），则尝试将请求的http方法与列表的控制器做匹配，若没有对应http方法的控制器，则抛该异常；
 *
 * HttpMediaTypeNotSupportedException：然后再对请求头与控制器支持的做比较，比如content-type请求头，若控制器的参数签名包含注解@RequestBody，但是请求的content-type请求头的值没有包含application/json，那么会抛该异常（当然，不止这种情况会抛这个异常）；
 *
 * MissingPathVariableException：未检测到路径参数。比如url为：/licence/{licenceId}，参数签名包含@PathVariable("licenceId")，当请求的url为/licence，在没有明确定义url为/licence的情况下，会被判定为：缺少路径参数；
 *
 * MissingServletRequestParameterException：缺少请求参数。比如定义了参数@RequestParam("licenceId") String licenceId，但发起请求时，未携带该参数，则会抛该异常；
 *
 * TypeMismatchException: 参数类型匹配失败。比如：接收参数为Long型，但传入的值确是一个字符串，那么将会出现类型转换失败的情况，这时会抛该异常；
 *
 * HttpMessageNotReadableException：与上面的HttpMediaTypeNotSupportedException举的例子完全相反，即请求头携带了"content-type: application/json;charset=UTF-8"，但接收参数却没有添加注解@RequestBody，或者请求体携带的 json 串反序列化成 pojo 的过程中失败了，也会抛该异常；
 *
 * HttpMessageNotWritableException：返回的 pojo 在序列化成 json 过程失败了，那么抛该异常；
 *
 * HttpMediaTypeNotAcceptableException：未知；
 *
 * ServletRequestBindingException：未知；
 *
 * ConversionNotSupportedException：未知；
 *
 * MissingServletRequestPartException：未知；
 *
 * AsyncRequestTimeoutException：未知；
 *
 * handleBindException
 *
 * 参数校验异常，后文详细说明。
 *
 * handleValidException
 *
 * 参数校验异常，后文详细说明。
 *
 * ### 自定义 ####
 *
 * handleBusinessException、handleBaseException
 *
 *
 * 处理自定义的业务异常，只是handleBaseException处理的是除了 BusinessException 意外的所有业务异常。就目前来看，这2个是可以合并成一个的。
 *
 *
 * handleException
 *
 *
 * 处理所有未知的异常，比如操作数据库失败的异常。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * @Validated 参数校验不通过
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult methodArgumentNotValidHandler(MethodArgumentNotValidException  e){
        log.error("参数校验失败！",e);
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(defaultMessage);
    }

    /**
     * @Validated 自定义异常
     */
    @ExceptionHandler(LqlCommonException.class)
    public AjaxResult LqlCommonExceptionHandler(LqlCommonException  e){
        log.error("内部校验失败！",e);
        return AjaxResult.error(e.getErrorCode(),e.getMessage());
    }
}
