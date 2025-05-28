package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author cxy784853792
 * @version 1.0
 * @description: TODO 套餐表
 * @date 2024/9/26 17:41
 */
@Mapper
public interface SetmealMapper {
    @Select("select count(*) from sky_take_out.setmeal where category_id=#{id}")
    Integer countByCategoryId(Long id);

    void insert(Setmeal setmeal);

    Page<SetmealVO> pageSearch(SetmealPageQueryDTO setmealPageQueryDTO);

    Setmeal getById(Long id);

    void update(Setmeal setmeal);

    @Delete("delete from sky_take_out.setmeal where id=#{id}")
    void deleteById(Long id);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
