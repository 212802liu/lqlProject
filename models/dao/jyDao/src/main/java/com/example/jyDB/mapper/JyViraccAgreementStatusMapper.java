package com.example.jyDB.mapper;

import com.example.jyDB.domain.JyViraccAgreementStatus;

/**
* @author lsw
* @description 针对表【AJFC_JY_VIRACC_AGREEMENT_STATUS(嘉银银行卡状态表)】的数据库操作Mapper
* @createDate 2024-11-25 16:49:26
* @Entity com.example.tbDB.domain.JyViraccAgreementStatus
*/
public interface JyViraccAgreementStatusMapper {

    int deleteByPrimaryKey(String id);

    int insert(JyViraccAgreementStatus record);

    int insertSelective(JyViraccAgreementStatus record);

    JyViraccAgreementStatus selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JyViraccAgreementStatus record);

    int updateByPrimaryKey(JyViraccAgreementStatus record);

}
