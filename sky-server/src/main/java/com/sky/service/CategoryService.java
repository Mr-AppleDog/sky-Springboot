package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @author cxy784853792
 * @version 1.0
 * @description: TODO
 * @date 2024/9/26 13:20
 */
public interface CategoryService {
    /**
     *  添加分类
     * @param categoryDTO 分类实体
     */
    void create(CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id 分类id
     */
    void delete(Long id);

    /**
     * 更新分类
     * @param categoryDTO 内容
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 启动或禁用
     * @param status 状态
     * @param id 分类ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 分页查询
     * @param categoryPageQueryDTO 分页条件DTO
     * @return PageResult
     */
    PageResult search(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据类型筛选
     * @param type 类型
     * @return 分类集合
     */
    List<Category> searchByType(Integer type);
}
