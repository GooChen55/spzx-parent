package com.spzx.product.controller;



import com.spzx.common.core.domain.R;
import com.spzx.common.core.web.controller.BaseController;
import com.spzx.common.core.web.domain.AjaxResult;
import com.spzx.common.core.web.page.TableDataInfo;
import com.spzx.common.log.annotation.Log;
import com.spzx.common.log.enums.BusinessType;
import com.spzx.common.security.annotation.InnerAuth;
import com.spzx.product.api.domain.Brand;
import com.spzx.product.service.IBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * http://localhost:9205/brand/list
 */
@Slf4j
@Tag(name="品牌管理")
@RequestMapping("/brand")
@RestController
public class BrandController extends BaseController {


    @Autowired
    private IBrandService brandService;

    @Operation(summary = "分页及带条件的查询")
    @GetMapping("/list")
    public TableDataInfo getPageList(String name){
        //调用父类的方法开启分页
        startPage();
        List<Brand> brandList = brandService.getPageList(name);
        return getDataTable(brandList);
    }

    @Log(title = "品牌管理",businessType = BusinessType.UPDATE)
    @Operation(summary = "添加或更新品牌")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    public AjaxResult saveOrUpdate(@RequestBody Brand brand){
        //调用IBrandService中添加或更新的方法
        brandService.saveOrUpdate(brand);
        return success();
    }

    @Operation(summary = "根据id查询品牌")
    @GetMapping("/{id}")
    public AjaxResult getBrandById(@PathVariable Long id){
        //调用IBrandService中根据id查询的方法
        Brand byId = brandService.getById(id);
        return success(byId);
    }

    @Operation(summary = "根据id删除品牌")
    @DeleteMapping("/{id}")
    public AjaxResult deleteBrandById(@PathVariable Long id){
        //调用IBrandService中根据id删除的方法
        brandService.removeById(id);
        return success();
    }

    @Operation(summary = "查询所有品牌，供分类管理使用")
    @GetMapping("/getBrandAll")
    public AjaxResult getBrandAll(){
        return success(brandService.selectBrandAll());
    }

    //=====给前台系统使用接口================================================
    @InnerAuth
    @Operation(summary = "获取全部品牌")
    @GetMapping("getBrandAllList")
    public R<List<Brand>> getBrandAllList() {
        return R.ok(brandService.selectBrandAll());
    }

}
