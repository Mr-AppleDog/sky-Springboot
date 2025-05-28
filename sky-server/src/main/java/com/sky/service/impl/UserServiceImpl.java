package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MrLu
 * @version 1.0
 * @description: 小程序d登录服务实现类
 * @date 2025/4/26 18:34
 */
@Service
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) throws IOException {
        //调用微信接口服务 获取openid
        String openid = getOpenID(userLoginDTO.getCode());
        //判断openid是否为空
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断用户是否存在，不存在则自动注册
        User user=userMapper.getByOpenId(openid);
        if (user==null){
            user= User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //返回用户信息
        return user;
    }

    private String getOpenID(String code){
        Map<String, String> paramMap=new HashMap<>();
        paramMap.put("appid",weChatProperties.getAppid());
        paramMap.put("secret",weChatProperties.getSecret());
        paramMap.put("js_code",code);
        paramMap.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
