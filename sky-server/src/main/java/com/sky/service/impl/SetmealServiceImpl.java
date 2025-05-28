package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2025/4/25 20:16
 */
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //向套餐表里插入数据
        setmealMapper.insert(setmeal);
        //获取插入ID
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageSearch(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        Page<SetmealVO> page = setmealMapper.pageSearch(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithFlavor(Long id) {
       Setmeal setmeal= setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO=new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
       Setmeal setmeal=new Setmeal();
       BeanUtils.copyProperties(setmealDTO,setmeal);

       // 修改套餐
        setmealMapper.update(setmeal);

        // 删除套餐关联的菜品
        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteByid(setmealId);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        //重新插入
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void delete(List<Long> ids) {
       ids.forEach(id -> {
           Setmeal setmeal = setmealMapper.getById(id);
           if(StatusConstant.ENABLE == setmeal.getStatus()){
               //起售中的套餐不能删除
               throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
           }
       });
       ids.forEach(id -> {
           //删除套餐表
           setmealMapper.deleteById(id);
           //删除套餐菜品关系表
           setmealDishMapper.deleteByid(id);
       });

    }

    @Override
    public void setmealStatus(Integer status, Long id) {
        //起售状态时，要判断菜品有没有停售的
        if(status==StatusConstant.ENABLE){
           List<Dish> dishList= dishMapper.getBySetmealId(id);
           if(dishList !=null && dishList.size()>0){
               dishList.forEach(dish -> {
                   if(StatusConstant.DISABLE == dish.getStatus()){
                       throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
                   }
               });
            }
        }
        Setmeal setmeal=Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}

