package com.spzx.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spzx.common.core.web.controller.BaseController;
import com.spzx.common.core.web.domain.AjaxResult;
import com.spzx.common.core.web.page.TableDataInfo;
import com.spzx.common.security.utils.SecurityUtils;
import com.spzx.product.domain.ProductUnit;
import com.spzx.product.service.IProductUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品单位")
@RestController
@RequestMapping("/productUnit")
public class ProductUnitController extends BaseController {

    @Autowired
    IProductUnitService productUnitService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    public TableDataInfo findPage(
                                  @Parameter(name = "pageNum",description = "第几页",required = true)
                                  @RequestParam(value = "pageNum",defaultValue = "1",required = true) Integer pageNum,
                                  @Parameter(name = "pageSize",description = "每页条数",required = true)
                                  @RequestParam(value = "pageSize",defaultValue = "10",required = true) Integer pageSize,
                                  ProductUnit productUnit){

        //com.baomidou.mybatisplus.core.metadata.IPage
        IPage pageParam = new Page(pageNum,pageSize);
        IPage page = productUnitService.findPage(pageParam,productUnit);
        return getDataTable(page);
    }

    /**
     * 新增商品单位
     */
    @Operation(summary = "新增商品单位")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated ProductUnit productUnit) {
        productUnit.setCreateBy(SecurityUtils.getUsername());
        return toAjax(productUnitService.insertProductUnit(productUnit));
    }


    @Operation(summary = "获取单位详情")
    @GetMapping("{id}")
    public AjaxResult getById(@PathVariable Long id) {
        return success(productUnitService.getById(id));
    }

    /**
     * 修改商品单位
     */
    @Operation(summary = "修改商品单位")
    @PutMapping
    public AjaxResult edit(@RequestBody @Validated ProductUnit productUnit) {
        productUnit.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(productUnitService.updateProductUnit(productUnit));
    }

    /**
     * 删除商品单位
     */
    @Operation(summary = "删除商品单位")
    @DeleteMapping("/{ids}")  //     /1,2,3
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(productUnitService.deleteProductUnitByIds(ids));
    }


    @Operation(summary = "获取全部单位")
    @GetMapping("getUnitAll")
    public AjaxResult selectProductUnitAll() {
        return success(productUnitService.list());
    }
}
