package com.spzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spzx.product.api.domain.Brand;

import java.util.List;

public interface IBrandService extends IService<Brand> {
    /**
     * 带条件查询的方法
     * @param name
     * @return
     */
    List<Brand> getPageList(String name);

    List<Brand> selectBrandAll();
}
