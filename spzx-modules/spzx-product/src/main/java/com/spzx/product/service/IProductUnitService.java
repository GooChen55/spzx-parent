package com.spzx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spzx.product.domain.ProductUnit;

public interface IProductUnitService extends IService<ProductUnit> {
    /**
     * 分页
     * @param pageParam 分页条件
     * @param productUnit 查询条件
     * @return 分页数据
     */
    IPage findPage(IPage pageParam, ProductUnit productUnit);

    /**
     * 保存单位
     * @param productUnit 表单数据
     * @return 成功结果
     */
    int insertProductUnit(ProductUnit productUnit);

    /**
     * 修改单位
     * @param productUnit 表单数据
     * @return 成功结果
     */
    int updateProductUnit(ProductUnit productUnit);

    /**
     * 删除单位数据
     * @param ids 一个或多个id
     * @return 成功结果
     */
    int deleteProductUnitByIds(Long[] ids);
}
