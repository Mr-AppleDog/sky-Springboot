package com.sky.service;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2025/4/29 18:40
 */
public interface ShoppingCartService {

    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车列表
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 清空购物车
     */
    void clean();
}
