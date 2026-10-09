package com.spzx.channel.service;

import com.spzx.product.api.domain.vo.CategoryVo;

import java.util.List;

public interface ICategoryService {
    List<CategoryVo> tree();
}
