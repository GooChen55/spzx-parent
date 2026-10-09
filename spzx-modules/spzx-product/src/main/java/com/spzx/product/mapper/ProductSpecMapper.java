package com.spzx.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spzx.product.domain.ProductSpec;

import java.util.List;

/**
 * 商品规格Mapper接口
 */
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {

    /**
     * 查询商品规格列表
     *
     * @param productSpec 商品规格
     * @return 商品规格集合
     */
    public List<ProductSpec> selectProductSpecList(ProductSpec productSpec);

}