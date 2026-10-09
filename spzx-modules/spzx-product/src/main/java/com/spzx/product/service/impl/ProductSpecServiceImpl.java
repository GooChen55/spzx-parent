package com.spzx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spzx.product.domain.ProductSpec;
import com.spzx.product.mapper.ProductSpecMapper;
import com.spzx.product.service.ICategoryService;
import com.spzx.product.service.IProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品规格Service业务层处理
 */
@Service
public class ProductSpecServiceImpl extends ServiceImpl<ProductSpecMapper, ProductSpec> implements IProductSpecService {

    @Autowired
    private ProductSpecMapper productSpecMapper;

    @Autowired
    private ICategoryService categoryService;

    /**
     * 查询商品规格列表
     *
     * @param productSpec 商品规格
     * @return 商品规格
     */
    @Override
    public List<ProductSpec> selectProductSpecList(ProductSpec productSpec) {
        return productSpecMapper.selectProductSpecList(productSpec);
    }


    /**
     * 查询商品规格
     *
     * @param id 商品规格主键
     * @return 商品规格
     */
    @Override
    public ProductSpec selectProductSpecById(Long id) {
        ProductSpec productSpec = productSpecMapper.selectById(id);
        //处理三级分类下拉回显问题
        List<Long> categoryIdList = categoryService.getAllCategoryIdList(productSpec.getCategoryId());
        productSpec.setCategoryIdList(categoryIdList);
        return productSpec;
    }

    @Override
    public List<ProductSpec> selectProductSpecListByCategoryId(Long categoryId) {
        List<ProductSpec> productSpecList = productSpecMapper
                .selectList(new LambdaQueryWrapper<ProductSpec>().eq(ProductSpec::getCategoryId, categoryId));
        return productSpecList;
    }
}