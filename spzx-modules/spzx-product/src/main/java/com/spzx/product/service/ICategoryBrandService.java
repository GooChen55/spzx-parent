package com.spzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spzx.product.api.domain.Brand;
import com.spzx.product.domain.CategoryBrand;

import java.util.List;

/**
 * 分类品牌Service接口
 */
public interface ICategoryBrandService extends IService<CategoryBrand> {

    /**
     * 查询分类品牌列表
     * @param categoryBrand
     * @return
     */
    List<CategoryBrand> selectCategoryBrandList(CategoryBrand categoryBrand);

    /**
     * 查询分类品牌对象信息
     * @param id 主键
     * @return 分类品牌对象
     */
    CategoryBrand selectCategoryBrandById(Long id);

    /**
     * 添加分类品牌
     * @param categoryBrand 表单数据(品牌id和第三级分类id)
     * @return
     */
    int insertCategoryBrand(CategoryBrand categoryBrand);

    /**
     * 修改分类品牌
     * @param categoryBrand 表单数据(主键，品牌id和第三级分类id)
     * @return
     */
    int updateCategoryBrand(CategoryBrand categoryBrand);

    /**
     * 根据分类查询品牌列表
     * @param categoryId 第三级分类id
     * @return 品牌列表
     */
    List<Brand> selectBrandListByCategoryId(Long categoryId);
}