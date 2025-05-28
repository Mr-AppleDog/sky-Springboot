package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cxy784853792
 * @version 1.0
 * @description: TODO
 * @date 2024/9/26 13:22
 */
@Mapper
public interface CategoryMapper {
    /**
     * 添加分类
     * @param category 分类
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into category (type, name, sort, status,create_time,update_time, create_user, update_user)" +
            "value " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void create(Category category);

    /**
     * 根据id 删除分类
     * @param id
     */
    @Delete("delete  from category where id=#{id}")
    void delete(Long id);


    /**
     * 更新
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO 分页条件
     * @return 分页page
     */
    Page<Category> pageSearch(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据类型查分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
