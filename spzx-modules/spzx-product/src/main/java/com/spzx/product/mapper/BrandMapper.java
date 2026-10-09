package com.spzx.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spzx.product.api.domain.Brand;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand> {
    List<Brand> getPageList(String name);

    List<Brand> findAll();
}
