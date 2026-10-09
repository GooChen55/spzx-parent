package com.spzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spzx.product.domain.ProductSpec;

import java.util.List;

/**
 * 商品规格Service接口
 */
public interface IProductSpecService extends IService<ProductSpec> {

    /**
     * 查询商品规格列表
     * @param productSpec 商品规格
     * @return 商品规格集合
     */
    public List<ProductSpec> selectProductSpecList(ProductSpec productSpec);

    /**
     * 查询商品规格
     *
     * @param id 商品规格主键
     * @return 商品规格
     */
    public ProductSpec selectProductSpecById(Long id);

    /**
     * 查询分类下的规格列表
     * @param categoryId 第三级分类id
     * @return 规格列表
     */
    List<ProductSpec> selectProductSpecListByCategoryId(Long categoryId);
}