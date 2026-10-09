package com.spzx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spzx.common.core.exception.ServiceException;
import com.spzx.product.api.domain.Brand;
import com.spzx.product.domain.CategoryBrand;
import com.spzx.product.mapper.CategoryBrandMapper;
import com.spzx.product.service.ICategoryBrandService;
import com.spzx.product.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类品牌Service业务层处理
 */
@Service
public class CategoryBrandServiceImpl extends ServiceImpl<CategoryBrandMapper, CategoryBrand> implements ICategoryBrandService {

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public List<CategoryBrand> selectCategoryBrandList(CategoryBrand categoryBrand) {
        return categoryBrandMapper.selectCategoryBrandList(categoryBrand);
    }

    @Override
    public CategoryBrand selectCategoryBrandById(Long id) {
        CategoryBrand categoryBrand = categoryBrandMapper.selectById(id);
        //需要处理分类下拉列选 回显问题：
        categoryBrand.setCategoryIdList(categoryService.getAllCategoryIdList(categoryBrand.getCategoryId()));
        return categoryBrand;
    }


    @Override
    public int insertCategoryBrand(CategoryBrand categoryBrand) {
        //验证数据是否已经存在，如果存在，则抛异常。
        CategoryBrand categoryBrandObj = categoryBrandMapper.selectOne(new LambdaQueryWrapper<CategoryBrand>()
                .eq(CategoryBrand::getBrandId, categoryBrand.getBrandId())
                .eq(CategoryBrand::getCategoryId, categoryBrand.getCategoryId()));
        if (categoryBrandObj != null) {
            throw new ServiceException("数据已经存在");
        }
        return categoryBrandMapper.insert(categoryBrand);
    }


    @Override
    public int updateCategoryBrand(CategoryBrand categoryBrand) {
        //1.表单数据没变化不更新数据
        CategoryBrand categoryBrandDB = categoryBrandMapper.selectById(categoryBrand.getId());
        if(categoryBrand.getBrandId().longValue() == categoryBrandDB.getBrandId().longValue() &&
                categoryBrand.getCategoryId().longValue() == categoryBrandDB.getCategoryId().longValue()){
            throw new ServiceException("请修改数据后提交");
        }
        //2.修改后的数据，不能是数据库里已存在的
        long count = categoryBrandMapper.selectCount(new LambdaQueryWrapper<CategoryBrand>()
                .eq(CategoryBrand::getBrandId, categoryBrand.getBrandId())
                .eq(CategoryBrand::getCategoryId, categoryBrand.getCategoryId()));
        if (count > 0) {
            throw new ServiceException("数据已经存在");
        }
        return categoryBrandMapper.updateById(categoryBrand);
    }


    @Override
    public List<Brand> selectBrandListByCategoryId(Long categoryId) {
        return categoryBrandMapper.selectBrandListByCategoryId(categoryId);
    }
}
