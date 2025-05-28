package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrLu
 * @version 1.0
 * @description: TODO
 * @date 2024/10/2 18:16
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private FlavorMapper flavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


    /**
     *  添加菜品和口味
     * @param dishDTO 菜品模型
     */
    @Override
    @Transactional
    public void insertDishAndFlavors(DishDTO dishDTO) {

        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //添加一天菜品数据
        dishMapper.insert(dish);
        Long id = dish.getId();
        //添加n条口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0){
            for (DishFlavor df: flavors) {
                df.setDishId(id);
            }
            flavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 分页查询
     * @param dishDTO 查询条件
     * @return 分页模型
     */
    @Override
    public PageResult search(DishPageQueryDTO dishPageQueryDTO) {
        //用PageHelper 分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //执行
        Page<Dish> page=dishMapper.pageSearch(dishPageQueryDTO);

        //获取结果和条数
        long total = page.getTotal();
        List<Dish> result = page.getResult();

        return new PageResult(total,result);
    }

    /**
     * 删除菜品
     * @param ids 需要删除的id 列表
     */
    @Override
    public void delete(List<Long> ids) {
        //判断能否删除=====起售还是停售
        for(Long id:ids){
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断能否删除=====是否在套餐里面
        List<SetmealDish> setmealDish=setmealDishMapper.getById(ids);
        if(setmealDish!=null &&setmealDish.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品
        //删除菜品关联口味
        for (Long id:ids) {
            dishMapper.deleteById(id);
            flavorMapper.deleteByid(id);
        }



    }

    /**
     * 菜品的停售
     * @param status 状态
     * @param id 商品id
     */
    @Override
    public void stareOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

    /**
     * 更新操作
     * @param dishDTO
     */
    @Override
    public void update(DishDTO dishDTO) {
        //修改菜品基本信息
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //删除口味信息
        flavorMapper.deleteByid(dishDTO.getId());
        //插入口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0){
            for (DishFlavor df: flavors) {
                df.setDishId(dish.getId());
            }
            flavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据id 获取菜品和口味
     * @param id 菜品编号
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据id 查询菜品数据
        Dish dish = dishMapper.getById(id);
        //根据id 查询口味数据
        List<DishFlavor> flavors=flavorMapper.getByDishId(id);
        
        //封装成vo
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * 根据id 查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
