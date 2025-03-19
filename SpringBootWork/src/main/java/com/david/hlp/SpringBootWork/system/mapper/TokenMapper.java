package com.david.hlp.SpringBootWork.system.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.david.hlp.SpringBootWork.system.token.Token;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface TokenMapper extends BaseMapper<Token> {

    @Select("SELECT * FROM token WHERE token = #{token}")
    Token findByToken(String token);

    @Select("SELECT expired = FALSE AND revoked = FALSE FROM token WHERE token = #{token}")
    Boolean isTokenValid(String token);

    @Insert("INSERT INTO token(user_id, token, token_type, expired, revoked) " +
            "VALUES(#{userId}, #{token}, #{tokenType}, #{expired}, #{revoked})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Token token);

    @Select("SELECT * FROM token WHERE user_id = #{userId} AND expired = false AND revoked = false")
    List<Token> findAllValidTokenByUser(Long userId);

    @Update({"<script>",
        "UPDATE token SET expired = #{expired}, revoked = #{revoked}",
        "WHERE token IN",
        "<foreach item='item' collection='list' open='(' separator=',' close=')'>",
        "#{item.token}",
        "</foreach>",
        "</script>"})
    void updateAll(@Param("list") List<Token> tokens);
}