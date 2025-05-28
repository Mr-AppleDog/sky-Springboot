package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

import java.io.IOException;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2025/4/26 18:25
 */
public interface UserService {
    //微信登录
    User wxLogin(UserLoginDTO userLoginDTO) throws IOException;
}
