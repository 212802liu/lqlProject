package com.example.auth.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import com.example.auth.conf.properties.CaptchaProperties;
import com.example.auth.service.ValidateCodeService;
import com.example.common.core.exception.LqlCommonException;
import com.example.common.core.web.AjaxResult;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @ClassName : ValidateCodeServiceImpl  //类名
 * @Description : 验证码生成实现类  //描述
 * @Author : liuql  //作者
 * @Date: 2024/9/17  11:12
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private CaptchaProperties captchaProperties;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Override
    public AjaxResult createCaptcha() throws IOException, LqlCommonException {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = captchaProperties.getEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        // 验证码开关是否启动
        if (!captchaEnabled){
            return ajax;
        }
        // 获取验证码 类型 算式、文字……
        String captchaType = captchaProperties.getType();

        // 生成验证码
        String capStr = null, code = null;
        BufferedImage image = null;
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        // 转换成图片流
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        // 验证码 保存redis
        String  uuid = UUID.fastUUID().toString();

        // 返回成功
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;

    }

    @Override
    public void checkCaptcha(String key, String value) throws LqlCommonException {

    }
}
