package com.spzx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spzx.common.core.utils.StringUtils;
import com.spzx.product.domain.ProductUnit;
import com.spzx.product.mapper.ProductUnitMapper;
import com.spzx.product.service.IProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class ProductUnitServiceImpl extends ServiceImpl<ProductUnitMapper, ProductUnit> implements IProductUnitService {

    @Autowired
    ProductUnitMapper productUnitMapper;


    @Override
    public IPage findPage(IPage pageParam, ProductUnit productUnit) {
        LambdaQueryWrapper<ProductUnit> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(productUnit.getName())) {
            queryWrapper.like(ProductUnit::getName, productUnit.getName());
        }
        //queryWrapper.select(ProductUnit::getName,ProductUnit::getCreateTime,ProductUnit::getId);
        IPage page = productUnitMapper.selectPage(pageParam, queryWrapper); //自动增加条件  and del_flag = 0
        return page;
        //return productUnitMapper.findPage(pageParam,productUnit); //参考课件，自己写sql实现分页；
    }

    @Override
    public int insertProductUnit(ProductUnit productUnit) {
        return productUnitMapper.insert(productUnit);
        //return this.save(productUnit) ? 1 : 0;
    }


    @Override
    public int updateProductUnit(ProductUnit productUnit) {
        return productUnitMapper.updateById(productUnit);
        //return this.updateById(productUnit) ? 1 : 0;
    }


    @Override
    public int deleteProductUnitByIds(Long[] ids) {
        return productUnitMapper.deleteBatchIds(Arrays.asList(ids));
        //return this.removeBatchByIds(Arrays.asList(ids)) ? 1 : 0;
    }
}
