package com.cys.mapper;

import com.cys.model.User; // 确保这里的 User 模型是您之前创建的那个
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @SuppressWarnings("SqlResolve")
    @Insert("insert into \"USER\" (name, account_id, token, gmt_create, gmt_modified) values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(User user);
}