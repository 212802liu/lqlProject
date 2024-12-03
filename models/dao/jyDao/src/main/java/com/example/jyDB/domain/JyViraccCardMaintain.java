package com.example.jyDB.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 嘉银虚账户请求绑定卡维护表
 * @TableName AJFC_JY_VIRACC_CARD_MAINTAIN
 */
@Data
public class JyViraccCardMaintain implements Serializable {
    /**
     * 请求流水号
     */
    private String loanReqNo;

    /**
     * 新还款账号
     */
    private String newAcno;

    /**
     * 新还款账号名
     */
    private String newAcname;

    /**
     * 变更原因
     */
    private String chgReason;

    /**
     * 是否有扣款授权书 1-是 0-否
     */
    private String ifAuth;

    /**
     * 操作类型 1新增 2删除
     */
    private String opType;

    /**
     * 账户类型 11-个人借记卡账户 12-企业账户
     */
    private String acType;

    /**
     * 证件类型 个人证件类型：0-身份证 1-户口薄 2-护照 3-军官证 4-士兵证 5-港澳居民来往内地通行证 6-台湾同胞来往内地通行证 7-临时身份证 8-外国人居留证 9-警官证 X-其他证件 企业证件类型：A-组织机构代码证号 B-营业执照号码 C-登记证书 D-国税登记证号码 E-地税登记证号码 F-开户许可证 G-事业单位编号 I-金融许可证编号 X-其他证件
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNo;

    /**
     * 银行预留手机号
     */
    private String phoneNo;

    /**
     * 换卡维护状态 I0初始化 0000为成功 9999为处理中 0001为失败
     */
    private String payStatus;

    /**
     * 失败原因
     */
    private String payFailMessage;

    /**
     * 虚拟账户号，商户系统中唯一编号
     */
    private String bizUserId;

    /**
     * 三方请求流水号
     */
    private String tranceNum;

    /**
     * 申请时间
     */
    private String transDate;

    /**
     *     银行名称
     */
    private String bankName;

    /**
     * 银行代码
     */
    private String bankCode;

    /**
     * 银行卡类型 1储蓄卡 2信用卡
     */
    private String cardType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 客户名称
     */
    private String custName;

    /**
     * 协议号
     */
    private String agreementNo;

    /**
     * 是否为平台老客户  0：tlt 1：yst
     */
    private String tltFlag;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        JyViraccCardMaintain other = (JyViraccCardMaintain) that;
        return (this.getLoanReqNo() == null ? other.getLoanReqNo() == null : this.getLoanReqNo().equals(other.getLoanReqNo()))
            && (this.getNewAcno() == null ? other.getNewAcno() == null : this.getNewAcno().equals(other.getNewAcno()))
            && (this.getNewAcname() == null ? other.getNewAcname() == null : this.getNewAcname().equals(other.getNewAcname()))
            && (this.getChgReason() == null ? other.getChgReason() == null : this.getChgReason().equals(other.getChgReason()))
            && (this.getIfAuth() == null ? other.getIfAuth() == null : this.getIfAuth().equals(other.getIfAuth()))
            && (this.getOpType() == null ? other.getOpType() == null : this.getOpType().equals(other.getOpType()))
            && (this.getAcType() == null ? other.getAcType() == null : this.getAcType().equals(other.getAcType()))
            && (this.getIdType() == null ? other.getIdType() == null : this.getIdType().equals(other.getIdType()))
            && (this.getIdNo() == null ? other.getIdNo() == null : this.getIdNo().equals(other.getIdNo()))
            && (this.getPhoneNo() == null ? other.getPhoneNo() == null : this.getPhoneNo().equals(other.getPhoneNo()))
            && (this.getPayStatus() == null ? other.getPayStatus() == null : this.getPayStatus().equals(other.getPayStatus()))
            && (this.getPayFailMessage() == null ? other.getPayFailMessage() == null : this.getPayFailMessage().equals(other.getPayFailMessage()))
            && (this.getBizUserId() == null ? other.getBizUserId() == null : this.getBizUserId().equals(other.getBizUserId()))
            && (this.getTranceNum() == null ? other.getTranceNum() == null : this.getTranceNum().equals(other.getTranceNum()))
            && (this.getTransDate() == null ? other.getTransDate() == null : this.getTransDate().equals(other.getTransDate()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankCode() == null ? other.getBankCode() == null : this.getBankCode().equals(other.getBankCode()))
            && (this.getCardType() == null ? other.getCardType() == null : this.getCardType().equals(other.getCardType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
            && (this.getAgreementNo() == null ? other.getAgreementNo() == null : this.getAgreementNo().equals(other.getAgreementNo()))
            && (this.getTltFlag() == null ? other.getTltFlag() == null : this.getTltFlag().equals(other.getTltFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLoanReqNo() == null) ? 0 : getLoanReqNo().hashCode());
        result = prime * result + ((getNewAcno() == null) ? 0 : getNewAcno().hashCode());
        result = prime * result + ((getNewAcname() == null) ? 0 : getNewAcname().hashCode());
        result = prime * result + ((getChgReason() == null) ? 0 : getChgReason().hashCode());
        result = prime * result + ((getIfAuth() == null) ? 0 : getIfAuth().hashCode());
        result = prime * result + ((getOpType() == null) ? 0 : getOpType().hashCode());
        result = prime * result + ((getAcType() == null) ? 0 : getAcType().hashCode());
        result = prime * result + ((getIdType() == null) ? 0 : getIdType().hashCode());
        result = prime * result + ((getIdNo() == null) ? 0 : getIdNo().hashCode());
        result = prime * result + ((getPhoneNo() == null) ? 0 : getPhoneNo().hashCode());
        result = prime * result + ((getPayStatus() == null) ? 0 : getPayStatus().hashCode());
        result = prime * result + ((getPayFailMessage() == null) ? 0 : getPayFailMessage().hashCode());
        result = prime * result + ((getBizUserId() == null) ? 0 : getBizUserId().hashCode());
        result = prime * result + ((getTranceNum() == null) ? 0 : getTranceNum().hashCode());
        result = prime * result + ((getTransDate() == null) ? 0 : getTransDate().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankCode() == null) ? 0 : getBankCode().hashCode());
        result = prime * result + ((getCardType() == null) ? 0 : getCardType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getAgreementNo() == null) ? 0 : getAgreementNo().hashCode());
        result = prime * result + ((getTltFlag() == null) ? 0 : getTltFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", loanReqNo=").append(loanReqNo);
        sb.append(", newAcno=").append(newAcno);
        sb.append(", newAcname=").append(newAcname);
        sb.append(", chgReason=").append(chgReason);
        sb.append(", ifAuth=").append(ifAuth);
        sb.append(", opType=").append(opType);
        sb.append(", acType=").append(acType);
        sb.append(", idType=").append(idType);
        sb.append(", idNo=").append(idNo);
        sb.append(", phoneNo=").append(phoneNo);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", payFailMessage=").append(payFailMessage);
        sb.append(", bizUserId=").append(bizUserId);
        sb.append(", tranceNum=").append(tranceNum);
        sb.append(", transDate=").append(transDate);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankCode=").append(bankCode);
        sb.append(", cardType=").append(cardType);
        sb.append(", createTime=").append(createTime);
        sb.append(", custName=").append(custName);
        sb.append(", agreementNo=").append(agreementNo);
        sb.append(", tltFlag=").append(tltFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}