package com.example.common.core.util;


import cn.hutool.core.io.FileUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用ftl模板填充数据，生成html文件工具类。
 * https://blog.csdn.net/Qzibidog/article/details/137600513?spm=1001.2014.3001.5501
 * @param <T>
 */
public class GenerateHtmlContextUtil<T> {
    /**
     * 本地临时地址
     */
    private static String TEMP_PATCH;
    /**
     * ftl模板地址
     */
    private static String TEMPLATE_FTL_PATH;
    /**
     * 是否需要删除临时生成的html文件
     */
    private Boolean NEED_DELETE_HTML = true;

    private  T data;
    private static Configuration CONFIG =  new Configuration(Configuration.getVersion());

    public final static String SUFFIX_FTL = ".ftl";
    public final static String SUFFIX_HTML = ".html";



    /**
     * 设置模板地址和临时文件地址
     * 删除HTTP临时文件
     * @param uploadPatch
     * @param templateFtlPath
     */
    public GenerateHtmlContextUtil(String uploadPatch, String templateFtlPath) {
        TEMP_PATCH = uploadPatch;
        TEMPLATE_FTL_PATH = templateFtlPath;
        NEED_DELETE_HTML = true;
    }
    /**
     * 设置模板地址和临时文件地址
     * @param uploadPatch
     * @param templateFtlPath
     * @param needDeleteHtml 是否需要删除生成的 html文件
     */
    public GenerateHtmlContextUtil(String uploadPatch, String templateFtlPath, boolean needDeleteHtml) {
        TEMP_PATCH = uploadPatch;
        TEMPLATE_FTL_PATH = templateFtlPath;
        NEED_DELETE_HTML = needDeleteHtml;
    }


    /**
     * 构建静态html
     **
      * @description:
      * @author:  liuql
      * @date: 2024/4/8 
      * @param fileName 不包括后缀名  
      *        
      */
    
    public  void buildStaticHtml(T data, String fileName) throws Exception {
        Map<String, Object> map = new HashMap<>(32);
        map.put("templateFtlPath", TEMPLATE_FTL_PATH);
        map.put("name", fileName);
        map.put("data", data);
        /// 放入文件后缀名
        map.put("suffix", SUFFIX_HTML);
        /// 生成静态html
        writerStaticFile(map);
    }

    /**
     * 页面静态化方法
     *
     * @param map 页面元素
     * @throws Exception
     */
    public  void writerStaticFile(Map<String, Object> map) throws Exception {
        //静态化
        File file = new File(TEMPLATE_FTL_PATH);
        File parentDirectory = file.getParentFile();

        CONFIG.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        FileTemplateLoader templateLoader=new FileTemplateLoader(new File(parentDirectory.getAbsolutePath()));

        CONFIG.setTemplateLoader(templateLoader);
        //获取模板
        Template temple = CONFIG.getTemplate(file.getName());
        //生成最终页面并写到文件
        Writer out = new OutputStreamWriter(new FileOutputStream(
                TEMP_PATCH + File.separator + map.get("name") + map.get("suffix")));
        try {
            //处理
            temple.process(map, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }



    /**
     * 读取静态html页面
     *
     * @param fileName 名称
     * @return staticHtml.toString() 静态页面html字符串
     */
    public  String generateStaticHtml(String fileName) {
        StringBuilder staticHtml = new StringBuilder(2048);

        try {

            FileReader fr = new FileReader(TEMP_PATCH + File.separator + fileName +SUFFIX_HTML );
            BufferedReader br = new BufferedReader(fr);

            String content = "";

            while ((content = br.readLine()) != null) {
                staticHtml.append(content);
            }

        } catch (Exception e) {
            return "";
        }
        // 删除临时文件
        if (NEED_DELETE_HTML){
            FileUtil.del(TEMP_PATCH + File.separator + fileName +SUFFIX_HTML);
        }
        return staticHtml.toString();
    }



}
