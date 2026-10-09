package com.spzx.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spzx.product.api.domain.ProductSku;
import com.spzx.product.api.domain.vo.SkuQuery;

import java.util.List;

/**
 * 商品skuMapper接口
 */
public interface ProductSkuMapper extends BaseMapper<ProductSku> {
    List<ProductSku> getTopSale();

    List<ProductSku> skuList(SkuQuery skuQuery);
}