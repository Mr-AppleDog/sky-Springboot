package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2025/4/26 18:49
 */
@Mapper
public interface UserMapper {
    @Select("select * from sky_take_out.user where openid=#{openid}")
    User getByOpenId(String openid);

    void insert(User user);
}
