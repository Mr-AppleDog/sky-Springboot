package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author cxy784853792
 * @version 1.0
 * @description:  分类管理
// * @date 2024/9/26 13:10
// */

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理")
public class CategoryController {
   @Autowired
   private CategoryService categoryService;

    @PostMapping
    @ApiOperation("添加分类")
    public Result create(@RequestBody CategoryDTO categoryDTO) {
        categoryService.create(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除分类")
    public Result delete(Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @PostMapping("status/{status}")
    @ApiOperation("禁用或启动分类")
    public Result startOrStop(@PathVariable Integer status ,Long id) {
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> search(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult=categoryService.search(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/list")
    @ApiOperation("类别查询")
    public Result<List<Category>> getCategoryByType(Integer type) {
        List<Category> list=categoryService.searchByType(type);
        return Result.success(list);
    }

}
