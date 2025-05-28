package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cxy784853792
 * @version 1.0
 * @description: TODO
 * @date 2024/9/26 13:20
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     *  添加分类
     * @param categoryDTO 分类实体
     */
    @Override
    public void create(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        //设置状态
        category.setStatus(StatusConstant.DISABLE);

        //设置更新和创建时间
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//
//        //设置修改人和创建人
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());

        //执行
        categoryMapper.create(category);
    }

    /**
     * 根据id删除
     * @param id 分类id
     */
    @Override
    public void delete(Long id) {
        Integer count=dishMapper.countByCategoryId(id);
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        count=setmealMapper.countByCategoryId(id);
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.delete(id);
    }

    /**
     * 更新
     * @param categoryDTO 内容
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        //更新时间和修改人
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());
        //执行
        categoryMapper.update(category);
    }

    /**
     *  分类的启用或禁用
     * @param status 状态
     * @param id 分类ID
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);
        categoryMapper.update(category);
    }

    /**
     * 分页查询
     * @param categoryPageQueryDTO 分页DTO
     * @return PageResult
     */
    @Override
    public PageResult search(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page=categoryMapper.pageSearch(categoryPageQueryDTO);
        // 获取结果的条数和数据
        long total = page.getTotal();
        List<Category> result = page.getResult();
        return new PageResult(total,result);
    }

    /**
     * 根据类型查分类
     * @param type 类型
     * @return
     */
    @Override
    public List<Category> searchByType(Integer type) {
        List<Category> list=categoryMapper.list(type);
        return list;
    }
}
