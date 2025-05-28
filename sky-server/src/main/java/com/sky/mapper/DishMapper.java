package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: 菜品操作
 * @date 2024/10/2 18:23
 */
@Mapper
public interface DishMapper {
    /**
     * 根据分类id统计所属分类的数量
     * @param id 分类id
     * @return 所属数量
     */
    @Select("select count(*) from sky_take_out.dish where category_id=#{id}")
    Integer countByCategoryId(Long id);

    /**
     * 插入菜品
     * @param dish 菜品
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询
     * @param dishPageQueryDTO 菜品模型
     * @return 集合
     */
    Page<Dish> pageSearch(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id 查询菜品
     * @param id 菜品id
     * @return 菜品
     */
    @Select("select * from sky_take_out.dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 删除商品
     * @param id 商品id
     */
    @Delete("delete from sky_take_out.dish where id=#{id}")
    void deleteById(Long id);

    /**
     * 更新菜品
     * @param dish 菜品
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 动态查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param id 套餐id
     * @return 菜品集合
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long id);
}
