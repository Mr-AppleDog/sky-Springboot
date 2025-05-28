package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2024/10/3 0:08
 */

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据 id列表查询
     * @param ids 列表
     * @return 集合
     */

    List<SetmealDish> getById(List<Long> ids);

    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long id);

    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteByid(Long setmealId);
}
