package com.yang.beetl.dao;

import com.yang.beetl.entity.User;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * 操作用户Dao
 */
@Component
public interface UserDao extends BaseMapper<User>{
}
