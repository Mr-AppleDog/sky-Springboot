package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.SetmealService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2025/4/29 18:40
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //构建dto 数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 判断当前商品是否在购物车中,如果在，数量加一，不在，添加到购物车
        List<ShoppingCart> shoppingCartList =shoppingCartMapper.list(shoppingCart);
        if(shoppingCartList!=null && shoppingCartList.size()>0){
            // 查到了，进行更新操作，获取对象
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingCartMapper.update(shoppingCart);
        }else {
            //进行添加
            Long setmealId = shoppingCart.getSetmealId();
            //判断时套餐还是菜品
            if(setmealId != null){
                //套餐
                SetmealVO byIdWithFlavor = setmealService.getByIdWithFlavor(shoppingCart.getSetmealId());
                shoppingCart.setName(byIdWithFlavor.getName());
                shoppingCart.setImage(byIdWithFlavor.getImage());
                shoppingCart.setAmount(byIdWithFlavor.getPrice());
            }else {
                //菜品
                Dish dish = dishMapper.getById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }


    }

    @Override
    public List<ShoppingCart> list() {
        Long currentId = BaseContext.getCurrentId();
            ShoppingCart shoppingCart = ShoppingCart.builder().userId(currentId).build();
            List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
            return shoppingCartList;
    }

    @Override
    public void clean() {
        Long currentId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteById(currentId);
    }
}
