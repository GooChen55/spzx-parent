package com.spzx.channel.controller;

import com.spzx.channel.service.ICategoryService;
import com.spzx.common.core.web.controller.BaseController;
import com.spzx.common.core.web.domain.AjaxResult;
import com.spzx.product.api.domain.vo.CategoryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "三级分类")
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @Autowired
    ICategoryService categoryService;

    @Operation(summary = "三级分类")
    @GetMapping("tree")
    public AjaxResult tree(){
        List<CategoryVo> treeList =  categoryService.tree();
        return success(treeList);
    }
}
