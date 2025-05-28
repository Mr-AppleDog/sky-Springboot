package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2025/4/29 18:46
 */
@Mapper
public interface ShoppingCartMapper {

     /**
      * 查询购物车
      * @param shoppingCart
      * @return
      */
     List<ShoppingCart> list(ShoppingCart shoppingCart);

     /**
      * 修改
      * @param shoppingCart
      */
     @Update("update sky_take_out.shopping_cart set number = #{number} where id = #{id}")
     void update(ShoppingCart shoppingCart);

     /**
      * 插入
      * @param shoppingCart
      */
     void insert(ShoppingCart shoppingCart);

     /**
      * 删除
      * @param currentId
      */
     @Delete("delete from sky_take_out.shopping_cart where user_id = #{currentId}")
     void deleteById(Long currentId);
}
