package com.example.jyDB.mapper;

import com.example.jyDB.domain.JyViraccCardMaintain;

/**
* @author lsw
* @description 针对表【AJFC_JY_VIRACC_CARD_MAINTAIN(嘉银虚账户请求绑定卡维护表)】的数据库操作Mapper
* @createDate 2024-11-25 16:37:36
* @Entity com.example.tbDB.domain.JyViraccCardMaintain
*/
public interface JyViraccCardMaintainMapper {

    int deleteByPrimaryKey(Long id);

    int insert(JyViraccCardMaintain record);

    int insertSelective(JyViraccCardMaintain record);

    JyViraccCardMaintain selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(JyViraccCardMaintain record);

    int updateByPrimaryKey(JyViraccCardMaintain record);

}
