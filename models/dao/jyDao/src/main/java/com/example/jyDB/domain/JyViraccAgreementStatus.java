package com.example.jyDB.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 嘉银银行卡状态表
 * @TableName AJFC_JY_VIRACC_AGREEMENT_STATUS
 */
@Data
public class JyViraccAgreementStatus implements Serializable {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 渠道编号
     */
    private String channelNo;

    /**
     * 虚账户号 商户系统唯一ID
     */
    private String bizUserId;

    /**
     * 证件类型 0-身份证
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNo;

    /**
     * 签约协议号
     */
    private String agreementNo;

    /**
     * 签约流水号
     */
    private String tranceNum;

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 银行预留手机号
     */
    private String phone;

    /**
     * 银行卡状态 0-失效 1-生效 2-冻结
     */
    private String status;

    /**
     * 客户名称
     */
    private String custName;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行代码
     */
    private String bankCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 子账户号
     */
    private String subAccno;

    /**
     * 子账户开通失败原因
     */
    private String applyErrorMsg;

    /**
     * 线下换绑卡标识，是否换绑卡 00：是 01：否
     */
    private String offlineChangeFlag;

    /**
     * 此卡是否属更改绑定卡 0：否 1：是
     */
    private String cardIsChangeBind;

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
        JyViraccAgreementStatus other = (JyViraccAgreementStatus) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getChannelNo() == null ? other.getChannelNo() == null : this.getChannelNo().equals(other.getChannelNo()))
            && (this.getBizUserId() == null ? other.getBizUserId() == null : this.getBizUserId().equals(other.getBizUserId()))
            && (this.getIdType() == null ? other.getIdType() == null : this.getIdType().equals(other.getIdType()))
            && (this.getIdNo() == null ? other.getIdNo() == null : this.getIdNo().equals(other.getIdNo()))
            && (this.getAgreementNo() == null ? other.getAgreementNo() == null : this.getAgreementNo().equals(other.getAgreementNo()))
            && (this.getTranceNum() == null ? other.getTranceNum() == null : this.getTranceNum().equals(other.getTranceNum()))
            && (this.getCardNo() == null ? other.getCardNo() == null : this.getCardNo().equals(other.getCardNo()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankCode() == null ? other.getBankCode() == null : this.getBankCode().equals(other.getBankCode()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSubAccno() == null ? other.getSubAccno() == null : this.getSubAccno().equals(other.getSubAccno()))
            && (this.getApplyErrorMsg() == null ? other.getApplyErrorMsg() == null : this.getApplyErrorMsg().equals(other.getApplyErrorMsg()))
            && (this.getOfflineChangeFlag() == null ? other.getOfflineChangeFlag() == null : this.getOfflineChangeFlag().equals(other.getOfflineChangeFlag()))
            && (this.getCardIsChangeBind() == null ? other.getCardIsChangeBind() == null : this.getCardIsChangeBind().equals(other.getCardIsChangeBind()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getChannelNo() == null) ? 0 : getChannelNo().hashCode());
        result = prime * result + ((getBizUserId() == null) ? 0 : getBizUserId().hashCode());
        result = prime * result + ((getIdType() == null) ? 0 : getIdType().hashCode());
        result = prime * result + ((getIdNo() == null) ? 0 : getIdNo().hashCode());
        result = prime * result + ((getAgreementNo() == null) ? 0 : getAgreementNo().hashCode());
        result = prime * result + ((getTranceNum() == null) ? 0 : getTranceNum().hashCode());
        result = prime * result + ((getCardNo() == null) ? 0 : getCardNo().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankCode() == null) ? 0 : getBankCode().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSubAccno() == null) ? 0 : getSubAccno().hashCode());
        result = prime * result + ((getApplyErrorMsg() == null) ? 0 : getApplyErrorMsg().hashCode());
        result = prime * result + ((getOfflineChangeFlag() == null) ? 0 : getOfflineChangeFlag().hashCode());
        result = prime * result + ((getCardIsChangeBind() == null) ? 0 : getCardIsChangeBind().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", channelNo=").append(channelNo);
        sb.append(", bizUserId=").append(bizUserId);
        sb.append(", idType=").append(idType);
        sb.append(", idNo=").append(idNo);
        sb.append(", agreementNo=").append(agreementNo);
        sb.append(", tranceNum=").append(tranceNum);
        sb.append(", cardNo=").append(cardNo);
        sb.append(", phone=").append(phone);
        sb.append(", status=").append(status);
        sb.append(", custName=").append(custName);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankCode=").append(bankCode);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", subAccno=").append(subAccno);
        sb.append(", applyErrorMsg=").append(applyErrorMsg);
        sb.append(", offlineChangeFlag=").append(offlineChangeFlag);
        sb.append(", cardIsChangeBind=").append(cardIsChangeBind);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}