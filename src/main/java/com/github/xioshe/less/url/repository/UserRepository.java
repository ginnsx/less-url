package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {
    int deleteByPrimaryKey(Long id);

    long insert(User record);

    long insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByUsername(String username);
}