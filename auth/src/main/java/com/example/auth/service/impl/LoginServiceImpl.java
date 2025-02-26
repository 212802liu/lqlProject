package com.example.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.auth.entity.LoginBody;
import com.example.auth.service.LoginService;
import com.example.common.core.constant.CommonRedisConstants;
import com.example.common.core.entity.user.LoginUser;
import com.example.common.core.exception.ResponseEnum;
import com.example.common.core.web.AjaxResult;
import com.example.common.redis.service.RedisClient;
import com.example.common.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TokenService tokenService;

    @Override
    public AjaxResult login(LoginBody loginBody) {

        // 数据库
        //模拟查询数据库 无数据
//        ResponseEnum.LICENCE_NOT_FOUND.assertNotNull(null);

        return  AjaxResult.success("这里的设计和`Servlet`中的`Filter`是相似的，当前过滤器可以决定是否执行下一个过滤器的逻辑，由`GatewayFilterChain#filter()`是否被调用来决定。而`ServerWebExchange`就相当于当前请求和响应的上下文。`ServerWebExchange`实例不单存储了`Request`和`Response`对象，还提供了一些扩展方法，如果想实现改造请求参数或者响应参数，就必须深入了解`ServerWebExchange`。\n" +
                "\n" +
                "# 理解 ServerWebExchange\n" +
                "\n" +
                "先看`ServerWebExchange`的注释：\n" +
                "\n" +
                "> Contract for an HTTP request-response interaction. Provides access to the HTTP request and response and also exposes additional server-side processing related properties and features such as request attributes.\n" +
                "\n" +
                "翻译一下大概是：\n" +
                "\n" +
                "> ServerWebExchange 是一个 HTTP 请求 - 响应交互的契约。提供对 HTTP 请求和响应的访问，并公开额外的[服务器](https://cloud.tencent.com/product/cvm/?from_column=20065&from=20065)端处理相关属性和特性，如请求属性。\n" +
                "\n" +
                "其实，`ServerWebExchange`命名为**服务网络交换器**，存放着重要的请求 - 响应属性、请求实例和响应实例等等，有点像`Context`的角色。\n" +
                "\n" +
                "## ServerWebExchange 接口\n" +
                "\n" +
                "`ServerWebExchange`接口的所有方法：\n" +
                "\n" +
                "```java\n" +
                "public interface ServerWebExchange {\n" +
                "\n" +
                "    // 日志前缀属性的KEY，值为org.springframework.web.server.ServerWebExchange.LOG_ID\n" +
                "    // 可以理解为 attributes.set(\"org.springframework.web.server.ServerWebExchange.LOG_ID\",\"日志前缀的具体值\");\n" +
                "    // 作用是打印日志的时候会拼接这个KEY对饮的前缀值，默认值为\"\"\n" +
                "    String LOG_ID_ATTRIBUTE = ServerWebExchange.class.getName() + \".LOG_ID\";\n" +
                "    String getLogPrefix();\n" +
                "\n" +
                "    // 获取ServerHttpRequest对象\n" +
                "    ServerHttpRequest getRequest();\n" +
                "\n" +
                "    // 获取ServerHttpResponse对象\n" +
                "    ServerHttpResponse getResponse();\n" +
                "    \n" +
                "    // 返回当前exchange的请求属性，返回结果是一个可变的Map\n" +
                "    Map<String, Object> getAttributes();\n" +
                "    \n" +
                "    // 根据KEY获取请求属性\n" +
                "    @Nullable\n" +
                "    default <T> T getAttribute(String name) {\n" +
                "        return (T) getAttributes().get(name);\n" +
                "    }\n" +
                "    \n" +
                "    // 根据KEY获取请求属性，做了非空判断\n" +
                "    @SuppressWarnings(\"unchecked\")\n" +
                "    default <T> T getRequiredAttribute(String name) {\n" +
                "        T value = getAttribute(name);\n" +
                "        Assert.notNull(value, () -> \"Required attribute '\" + name + \"' is missing\");\n" +
                "        return value;\n" +
                "    }\n" +
                "\n" +
                "     // 根据KEY获取请求属性，需要提供默认值\n" +
                "    @SuppressWarnings(\"unchecked\")\n" +
                "    default <T> T getAttributeOrDefault(String name, T defaultValue) {\n" +
                "        return (T) getAttributes().getOrDefault(name, defaultValue);" +
                " // 返回当前请求的网络会话\n" +
                "    Mono<WebSession> getSession();\n" +
                "\n" +
                "    // 返回当前请求的认证用户，如果存在的话\n" +
                "    <T extends Principal> Mono<T> getPrincipal();  \n" +
                "    \n" +
                "    // 返回请求的表单数据或者一个空的Map，只有Content-Type为application/x-www-form-urlencoded的时候这个方法才会返回一个非空的Map -- 这个一般是表单数据提交用到\n" +
                "    Mono<MultiValueMap<String, String>> getFormData();   \n" +
                "    \n" +
                "    // 返回multipart请求的part数据或者一个空的Map，只有Content-Type为multipart/form-data的时候这个方法才会返回一个非空的Map  -- 这个一般是文件上传用到\n" +
                "    Mono<MultiValueMap<String, Part>> getMultipartData();\n" +
                "    \n" +
                "    // 返回Spring的上下文// 这几个方法和lastModified属性相关\n" +
                "    boolean isNotModified();\n" +
                "    boolean checkNotModified(Instant lastModified);\n" +
                "    boolean checkNotModified(String etag);\n" +
                "    boolean checkNotModified(@Nullable String etag, Instant lastModified);\n" +
                "    \n" +
                "    // URL转换\n" +
                "    String transformUrl(String url);    \n" +
                "   \n" +
                "    // URL转换映射\n" +
                "    void addUrlTransformer(Function<String, String> transformer); \n" +
                "\n" +
                "    // 注意这个方法，方法名是：改变，这个是修改ServerWebExchange属性的方法，返回的是一个Builder实例，Builder是ServerWebExchange的内部类\n" +
                "    default Builder mutate() {\n" +
                "\t     return new DefaultServerWebExchangeBuilder(this);\n" +
                "    }\n" +
                "\n" +
                "    interface Builder {      \n" +
                "         \n" +
                "        // 覆盖ServerHttpRequest\n" +
                "        Builder request(Consumer<ServerHttpRequest.Builder> requestBuilderConsumer);\n" +
                "        Builder request(ServerHttpRequest request);\n" +
                "        \n" +
                "        // 覆盖ServerHttpResponse");
        //验证码 ； 成功后作废
//        String code =  redisClient.get(CommonRedisConstants.PREFIX_REDIS_CAPTCHA + loginBody.getUuid());
//        if (StrUtil.isBlank(code) || !code.equals(loginBody.getValidateCode())){
//            return AjaxResult.error("验证码错误或已过期");
//        }
//        // token
//        LoginUser loginUser = new LoginUser();
//        String token = tokenService.createToken(loginUser);
//        return AjaxResult.success("成功",token);
    }
}
