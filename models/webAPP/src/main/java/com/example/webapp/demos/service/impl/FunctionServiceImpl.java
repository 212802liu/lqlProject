package com.example.webapp.demos.service.impl;

import com.example.common.core.util.GenerateHtmlContextUtil;
import com.example.webapp.demos.service.FunctionService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.prng.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
public class FunctionServiceImpl implements FunctionService
{
    @Override
    public String htmlContentMail()  {
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> weiXinPay = generateContentMailData("weiXinPayLoanInfoList");
        Map<String,Object> zhiFuPay = generateContentMailData("zhiFuPayLoanInfoList");
        data.put("weiXinPay",weiXinPay);
        data.put("zhiFuPay",zhiFuPay);
        data.put("maxLength",Math.max((Integer) zhiFuPay.get("size"),(Integer) weiXinPay.get("size")));
//        data.put("rowspan",Math.max((Integer) zhiFuPay.get("size"),(Integer) weiXinPay.get("size"))+1);
        data.put("period","测试");
        try {
            GenerateHtmlContextUtil generateHtmlContextUtil = new GenerateHtmlContextUtil("C:\\Users\\lsw\\Desktop\\temp\\temp", "E:\\gitwork\\lqlProject\\models\\webAPP\\src\\main\\resources\\mailContent.tlt");
            generateHtmlContextUtil.buildStaticHtml(data,"htmlContent");
        }catch (Exception e){
            log.error("失败",e);
        }



        return "OK";
    }

    private Map<String, Object> generateContentMailData(String name) {
        Random rom = new Random();
        int anInt = rom.nextInt(10)+3;
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("applyNum",anInt);
        dataMap.put("applyPassNum",anInt-1);
        dataMap.put("applyRefuseNum",1);
        dataMap.put("applyMsg","申请不喜欢");

        dataMap.put("useNum",anInt);
        dataMap.put("usePassNum",2);
        dataMap.put("useRefuseNum",anInt-2);
        dataMap.put("useMsg","讨厌");
        List<Map<String,Object>> weiXinPayLoanInfoList = new ArrayList<>();
        int size = 3;
        for (int i = 0; i < size; i++) {
            Map<String,Object> itemMap = new HashMap<>();
            itemMap.put("subjectId","ajxt"+i);
            itemMap.put("num",i);
            itemMap.put("loanAmt",rom.nextDouble());
            weiXinPayLoanInfoList.add(itemMap);
        }
        dataMap.put(name,weiXinPayLoanInfoList);
        dataMap.put("size",size);
        return dataMap;
    }
}
