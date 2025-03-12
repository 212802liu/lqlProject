package com.example.common.core.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * redis 或者 数据库
 */
@Data
public class CommonCompensationTemplate {
    Integer id;
    String channel;

    String fileDesc;
    /**
     * 上传sftp 信息
     */
    String uploadSftpPath;
    private String ftpUrl;
    private int port;
    private String userName;
    private String passPwd;

    /**
     * 文本信息
     */

    String title;
    String content;
    String  otherCompany;

    String OurCompany;

    String  postscript;
    /**
     * 备用字段
     */
    String add1;
    String add2;
    /**
     * 表格信息
     */
    /**
     * 表头信息。例如： 计划序号,代偿金额,期数,客户姓名
     */
    String headerInformation;
    /**
     * 表头尺寸信息, 例如：30,30,30,30
     * 不填 默认 30
     */
    String headerSizeInformation;
    /**
     * 数据信息。subjectId,repayAmt,term,customerName
     */
    String dataInformation;

    String needDelete;

    /**
     * 数据库不用存
     * 文本替换列表或map
     */
    List<String> replaceValueList;
    Map<String,Object> replaceValueMap;


}
