package com.example.common.redis.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Redis使用FastJson序列化
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = {"org.springframework", "com.example"};
    // 使用 JSONReader.autoTypeFilter 可以有效地控制和限制反序列化过程中的类型安全性，防止潜在的安全漏洞，确保只有经过验证的类可以被实例化。
    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(JSON_WHITELIST_STR);

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        /**
         * JSONWriter.Feature... features 是一个可变参数，允许你在序列化时指定多个特性（features），以控制序列化的行为。
         * JSONWriter.Feature.WriteClassName 在序列化时，WriteClassName 会在 JSON 输出中添加一个 @type 字段，表示对象的具体类型。
         * {
         *     "@type": "com.example.MyClass",
         *     "field1": "value1",
         *     "field2": 123
         * }
         */

        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }
}
