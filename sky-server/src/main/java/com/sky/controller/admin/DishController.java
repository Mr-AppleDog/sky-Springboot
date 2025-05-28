package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author MrLu
 * @version 1.0
 * @description: 菜品相关接口
 * @date 2024/10/2 18:12
 */
@RestController
@RequestMapping("/admin/dish")
@ApiOperation("菜品相关操作")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate  redisTemplate;

    /**
     * 添加菜品
     * @param dishDTO 菜品模型
     * @return 成功
     */
    @PostMapping
    @ApiOperation("添加菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.insertDishAndFlavors(dishDTO);

        //清理缓存数据
        Long categoryId = dishDTO.getCategoryId();
        String key="dish_"+categoryId;
        cleanCache(key);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> search(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult=dishService.search(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteById( @RequestParam List<Long> ids){
        dishService.delete(ids);
        cleanCache("dish_*");
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品的停售")
    public Result startOrStop(@PathVariable Integer status,Long id){
        dishService.stareOrStop(status,id);
        cleanCache("dish_*");
        return Result.success();
    }

    @PutMapping()
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        dishService.update(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable  Long id){
        DishVO dishVO=dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }


    private void cleanCache(String pattern){
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);

    }
}
