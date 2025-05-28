package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2024/10/2 18:16
 */
public interface DishService {
    /**
     *  添加菜品和口味
     * @param dishDTO 菜品模型
     */
    void insertDishAndFlavors(DishDTO dishDTO);

    /**
     *  分页查询
     * @param dishDTO 查询条件
     * @return 分页模型
     */
    PageResult search(DishPageQueryDTO dishDTO);

    /**
     * 删除菜品
     * @param ids 需要删除的id 列表
     */
    void delete(List<Long> ids);

    /**
     * 菜品的停售
     * @param status 状态
     * @param id 商品id
     */
    void stareOrStop(Integer status, Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 根据菜品id 查询菜品和口味
     * @param id 菜品编号
     * @return dishVo
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 根据菜品分类id 查询菜品
     * @param categoryId 分类id
     * @return 菜品列表
     */
    List<Dish> list(Long categoryId);


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
