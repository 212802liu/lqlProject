package com.example.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 断言+统一异常处理 ：https://www.jianshu.com/p/3f3d9e8d1efa
 * https://mp.weixin.qq.com/s?__biz=MzkzNzI2OTkzNg==&mid=2247490967&idx=1&sn=070c93443c1de6074978b0fcac34a254&chksm=c2935248f5e4db5e7a28e99a21ee548ec22c5bcf5243ba47803d13f92f12ada08530ffcd4ede&scene=132&exptype=timeline_recommend_article_extendread_samebiz#wechat_redirect
 */
@Getter
@AllArgsConstructor
public enum  ResponseEnum implements LqlCommonExceptionAssert{
    /**
     * Bad licence type
     */
    BAD_LICENCE_TYPE("7001", "Bad licence type."),
    /**
     * Licence not found
     */
    LICENCE_NOT_FOUND("7002", "Licence not found."),
    USER_NOT_FOUND("7003", "user not found.")
    ;

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

}
