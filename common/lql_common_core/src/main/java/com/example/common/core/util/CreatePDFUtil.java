package com.example.common.core.util;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.example.common.core.entity.CommonCompensationTemplate;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 看似一个工具类
 * 实际应用面 暂时不广
 */
@Slf4j
public class CreatePDFUtil {

    public static void main(String[] args) throws Exception {
        CommonCompensationTemplate ct = new CommonCompensationTemplate();
        ct.setChannel("HB");
        ct.setFileDesc("代偿证明");
        ct.setUploadSftpPath("/public/upload/hb/dcProof/{date}/");
        ct.setFtpUrl("192.168.32.194");
        ct.setPort(18299);
        ct.setPassPwd("hb123456");
        ct.setUserName("hb_sftp");

        ct.setTitle("代偿证明");
        ct.setContent("旧金山xxxx公司（以下称“旧金山”）运营网络平台“借钱”（网页端网址为https://www.xxx.com，APP端名称为“借钱”）" +
                "（以下称“借钱”或“平台”）。纽约xxxx公司（以下称“我司”）与旧金山签署有《获客顾问服务协议》、我司与天地一家融资担保（福建）" +
                "有限公司（以下称“天地一家”）签署有《担保服务合作协议》，就我司与旧金山、天地一家进行合作，由“借钱”向我司推荐借款人，我司向借款人" +
                "发放借款资金，天地一家就我司对借款人享有的债权向我司提供担保等相关事宜进行了约定。经我司确认，暂计{}（含），附件列表中" +
                "所示的我司对借款人享有的债权（包括但不限于借款本金、利息、罚息、违约金等）（以下称“确认标的”）已全部由天地一家完成担保。天地一家已向我司" +
                "支付完毕确认标的债权的全部对价，天地一家有权依法向借款人追偿。");
        ct.setOtherCompany("鉴于");
        ct.setOurCompany("纽约xxxx公司");
        ct.setPostscript("附：华盛顿有点钱公司代为清偿债权清单");

//        ct.setTableColumn(8);
        ct.setHeaderInformation("计划序号,代偿金额,期数");
        ct.setDataInformation("subjectId,repayAmt,term");

        String dcDate = DateUtil.format(new Date(),"yyyyMMdd");
        // todo 待确认
        String chinese_date = dcDate.substring(0,4)+"年"+dcDate.substring(4,6)+"月"+dcDate.substring(6,8)+"日";
        ct.setReplaceValueList(Collections.singletonList(chinese_date));

        List<Map<String,Object>> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String,Object> item = new HashMap<>();
            item.put("subjectId","subjectId"+i);
            item.put("repayAmt",i);
            item.put("term","term"+i);
            data.add(item);
        }


        createPDF("E:\\home\\ajxt\\file\\test.pdf",data,ct);

    }

    /**
     * the name of the font or its location on file
     */
    final static String FONT_NAME = "STSongStd-Light";
    /**
     * he encoding to be applied to this font
     */
    final static String FONT_ENCODING = "UniGB-UCS2-H";


    private static void createPDF(String localFilePath, List<Map<String, Object>> dp, CommonCompensationTemplate channelCompensationTemplate) throws Exception{
        log.info("createPDF","生成pdf    开始");
        Document document = new Document(PageSize.A4,90, 90, 30, 40);
        try{
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(localFilePath));
            writer.setPageEvent(new FooterEvent());
            document.open();

            String title = channelCompensationTemplate.getTitle();
            // 动态文本替换
            String content = StrUtil.format(channelCompensationTemplate.getContent(),channelCompensationTemplate.getReplaceValueList().toArray());
            content = StrUtil.format(content,channelCompensationTemplate.getReplaceValueMap());

            String otherCompany = channelCompensationTemplate.getOtherCompany();
            String OurCompany = channelCompensationTemplate.getOurCompany();

            String postscript = channelCompensationTemplate.getPostscript();

            //标题格式
            Paragraph titleParagraph = new Paragraph(title, getChineseTitleFont());
            titleParagraph.setAlignment(Element.ALIGN_CENTER);// 居中
            titleParagraph.setFirstLineIndent(24);// 首行缩进
            titleParagraph.setLeading(35f);// 行间距
            titleParagraph.setSpacingBefore(5f);// 设置上空白
            titleParagraph.setSpacingAfter(10f);// 设置段落下空白

            //鉴于
            Paragraph otherCompanyParagraph = null;
            if (StrUtil.isNotBlank(otherCompany)) {
                otherCompanyParagraph = new Paragraph(otherCompany, getContentChineseFont());
                otherCompanyParagraph.setAlignment(Element.ALIGN_LEFT);// 对齐方式
                otherCompanyParagraph.setFirstLineIndent(20);// 首行缩进
                otherCompanyParagraph.setLeading(22f);// 行间距
            }


            //内容
            Paragraph contentParagraph = new Paragraph(content, getContentChineseFont());
            contentParagraph.setAlignment(Element.ALIGN_LEFT);
            contentParagraph.setFirstLineIndent(20);// 首行缩进
            contentParagraph.setLeading(22f);// 行间距

            //自己公司
            Paragraph OurCompanyParagraph = new Paragraph(OurCompany, getContentChineseFont());
            OurCompanyParagraph.setAlignment(Element.ALIGN_RIGHT);
            OurCompanyParagraph.setLeading(22f);// 行间距

            //签署日期
            String dcDate = DateUtil.format(new Date(),"yyyyMMdd");
            String signTime = dcDate.substring(0,4)+"年"+dcDate.substring(4,6)+"月"+dcDate.substring(6,8)+"日";
            Paragraph signTimeParagraph = new Paragraph(signTime, getContentChineseFont());
            signTimeParagraph.setAlignment(Element.ALIGN_RIGHT);
            signTimeParagraph.setLeading(22f);// 行间距

            //添加段落
            document.add(titleParagraph);
            if (StrUtil.isNotBlank(otherCompany)) {
                document.add(otherCompanyParagraph);
            }
            document.add(contentParagraph);
            document.add(OurCompanyParagraph);
            document.add(signTimeParagraph);

            //新一页
            document.newPage();
            // 附：xxxx公司代为清偿债权清单
            if (StrUtil.isNotBlank(postscript)){
                Paragraph postscriptParagraph = new Paragraph(postscript, getContentChineseFont());
                postscriptParagraph.setAlignment(Element.ALIGN_LEFT);
                postscriptParagraph.setLeading(50f);// 行间距
                postscriptParagraph.setSpacingAfter(5f);
                document.add(postscriptParagraph);
            }
            //生成表格数据
            if (StrUtil.isNotBlank(channelCompensationTemplate.getHeaderInformation())){
                PdfPTable table = createTable(dp,channelCompensationTemplate);
                document.add(table);
            }

            log.info(channelCompensationTemplate.getChannel()+channelCompensationTemplate.getFileDesc()+":createPDF 生成pdf    完成");
        }catch (Exception e) {
            log.error(channelCompensationTemplate.getChannel()+channelCompensationTemplate.getFileDesc()+":createPDF 生成pdf文件异常",e);
            throw new RuntimeException(channelCompensationTemplate.getChannel()+channelCompensationTemplate.getFileDesc()+":createPDF生成pdf文件异常",e);
        } finally {
            document.close();
        }
    }

    /**
     * 生成表格
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    private static  PdfPTable createTable(List<Map<String, Object>> dp, CommonCompensationTemplate channelCompensationTemplate) throws IOException, DocumentException, RuntimeException {
        log.info("createTable","生成table表格--开始");
        // 中文字体
        BaseFont bfChinese = BaseFont.createFont(FONT_NAME, FONT_ENCODING,BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfChinese, 6, Font.NORMAL);


        // 基本信息 表头
        String[] headerInformations = channelCompensationTemplate.getHeaderInformation().split(",");
        // 列宽度
        String[] headerSizeInformations = null;
        if (StrUtil.isNotBlank(channelCompensationTemplate.getHeaderSizeInformation())){
            headerSizeInformations = channelCompensationTemplate.getHeaderSizeInformation().split(",");
        }
        // 数据信息
        String[] dataInformations = channelCompensationTemplate.getDataInformation().split(",");

        if (dataInformations.length != headerInformations.length){
            log.error(channelCompensationTemplate.getChannel()+"模版配置有误，表头列数与数据列数 不一致");
            throw new RuntimeException(channelCompensationTemplate.getChannel()+"模版配置有误，表头列数与表头宽度个数 不一致");
        }
        //生成一个9列的表格
        PdfPTable table = new PdfPTable(dataInformations.length);
        table.setTotalWidth(PageSize.A4.getWidth()-190);
        table.setLockedWidth(true);
        // 设置表头
        for (int i = 0; i < headerInformations.length; i++) {
            PdfPCell cell = new PdfPCell(new Phrase(headerInformations[i],fontChinese));

            int size = ArrayUtil.isEmpty(headerSizeInformations) ?30:Integer.parseInt(headerSizeInformations[i]);
            fontStyle(cell,1,1,size);
            table.addCell(cell);
        }
        // 设置内容
        try {
            // 判断查询有值后循环添加数据
            if (dp != null && dp.size() > 0){
                for (int i = 0; i < dp.size(); i++) {
                    for (int j = 0; j < dataInformations.length; j++) {
                        int size = ArrayUtil.isEmpty(headerSizeInformations) ?30:Integer.parseInt(headerSizeInformations[i]);
                        PdfPCell cell = new PdfPCell(new Phrase( Optional.ofNullable(dp.get(i).get(dataInformations[j])).orElse("").toString(), fontChinese));
                        fontStyle(cell,1, 1, size);
                        table.addCell(cell);
                    }
                }
            }
        } catch (Exception e) {
            log.error("createTable","生成pdf文件中的表格异常",e);
            throw new RuntimeException("createTable 生成pdf文件中的表格异常",e);
        }
        log.info("createTable","生成table表格--结束");
        return table;
    }

    /**
     * table表格字体样式
      */

    private static void fontStyle(PdfPCell cell,int col,int row,int size){
        cell.setColspan(col);//设置所占列数
        cell.setRowspan(row);//合并行
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
        cell.setFixedHeight(size);//设置高度
    }

    //支持中文 设置字体，字体颜色、大小等
    public static Font getChineseTitleFont() throws Exception {
        Font ChineseFont = null;
        try {
            BaseFont simpChinese = BaseFont.createFont(FONT_NAME, FONT_ENCODING, BaseFont.NOT_EMBEDDED);
            ChineseFont = new Font(simpChinese, 16, Font.BOLD, BaseColor.BLACK);
//            BaseFont microsoftYaheiFont = BaseFont.createFont("MSYH.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            ChineseFont = new Font(microsoftYaheiFont, 26, Font.BOLD, BaseColor.BLACK);
        } catch (DocumentException e) {
            log.error("getChineseFont" ,"字体异常",e);
            throw new RuntimeException("getChineseTitleFont 字体异常",e);
        } catch (IOException e) {
            log.error("getChineseFont" ,"字体异常",e);
            throw new RuntimeException("getChineseTitleFont 字体异常",e);
        }
        return ChineseFont;
    }

    //支持中文 设置字体，字体颜色、大小等
    public static Font getContentChineseFont() throws Exception {
        BaseFont simpChinese;
        Font ChineseFont = null;
        try {
            simpChinese = BaseFont.createFont(FONT_NAME, FONT_ENCODING, BaseFont.NOT_EMBEDDED);
            ChineseFont = new Font(simpChinese, 11, Font.NORMAL, BaseColor.BLACK);
        } catch (DocumentException e) {
            log.error("getContentChineseFont" ,"字体异常",e);
            
            throw new RuntimeException("getContentChineseFont字体异常",e);
        } catch (IOException e) {
            log.error("getContentChineseFont" ,"字体异常",e);
            throw new RuntimeException("getContentChineseFont字体异常",e);
        }
        return ChineseFont;
    }
    //支持中文 设置字体，字体颜色、大小等
    public static Font getContentChineseFont_red() throws RuntimeException {
        BaseFont simpChinese;
        Font ChineseFont = null;
        try {
            simpChinese = BaseFont.createFont(FONT_NAME, FONT_ENCODING, BaseFont.NOT_EMBEDDED);
            ChineseFont = new Font(simpChinese, 11, Font.NORMAL, BaseColor.RED);

//            BaseFont microsoftYaheiFont = BaseFont.createFont("MSYH.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            ChineseFont = new Font(microsoftYaheiFont, 15, Font.NORMAL, BaseColor.RED);
        } catch (DocumentException e) {
            log.error("getContentChineseFont" ,"字体异常",e);
            throw new RuntimeException("getContentChineseFont字体异常",e);
        } catch (IOException e) {
            log.error("getContentChineseFont" ,"字体异常",e);
            throw new RuntimeException("getContentChineseFont字体异常",e);
        }
        return ChineseFont;
    }

    /**
     * 页脚展示页数
     */
    public static class FooterEvent extends PdfPageEventHelper {
        //总页数
        PdfTemplate totalPage;
        //字体
        Font font;

        {
            try {
                BaseFont chinese = BaseFont.createFont(FONT_NAME, FONT_ENCODING, BaseFont.NOT_EMBEDDED);
                font = new Font(chinese,10);
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 文档打开时创建PdfTemplate对象，用于存储总页数的占位符。
         */
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            totalPage = cb.createTemplate(50, 9);
        }

        /**
         * 每生成一页时插入当前页码和总页数占位符。
         * @param writer the <CODE>PdfWriter</CODE> for this document
         * @param document the document
         */

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            //创建一个两列的表格
            PdfPTable table = new PdfPTable(2);
            try {
                table.setTotalWidth(PageSize.A4.getWidth());//总宽度为A4纸张宽度
                table.setLockedWidth(true);//锁定列宽
                table.setWidths(new int[]{50, 50});//设置每列宽度
                // 左单元格：当前页码（如"第1页，共"）
                PdfPCell cell = new PdfPCell(new Phrase("第"+document.getPageNumber() + " 页，共", font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);//设置水平右对齐
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
                cell.disableBorderSide(15);//隐藏全部边框
                table.addCell(cell);
                // 右单元格：总页数占位符（通过Image实例化模板）
                PdfPCell cell1 = new PdfPCell(Image.getInstance(totalPage));//共 页
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);//设置水平左对齐
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直居中
                cell1.disableBorderSide(15);//隐藏全部边框
                table.addCell(cell1);
                // 将表格写入页脚位置（坐标原点为页面左下角）
                table.writeSelectedRows(0, -1, 0, 30, writer.getDirectContent());
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }

        /**
         * 文档关闭时获取最终总页数，填充总页数到模板
         * @param writer the <CODE>PdfWriter</CODE> for this document
         * @param document the document
         */
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            String text = "" +String.valueOf(writer.getPageNumber()) +"页";
            ColumnText.showTextAligned(totalPage, Element.ALIGN_MIDDLE, new Paragraph(text, font), 0, 0, 0);
        }
    }

}
