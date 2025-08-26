package com.cys.mapper;

import com.cys.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @SuppressWarnings("SqlResolve")
    @Insert("insert into \"USER\" (name, account_id, token, gmt_create, gmt_modified, avatar_url, bio, public_repos, followers, following, html_url) " +
            "values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl}, #{bio}, #{public_repos}, #{followers}, #{following}, #{html_url})")
    void insert(User user);

    @SuppressWarnings("SqlResolve")
    @Select("select * from \"USER\" where token = #{token}")
    User findByToken(@Param("token") String token);
}