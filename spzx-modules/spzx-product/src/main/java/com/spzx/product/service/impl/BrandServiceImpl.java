package com.spzx.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spzx.product.api.domain.Brand;
import com.spzx.product.mapper.BrandMapper;
import com.spzx.product.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper,Brand> implements IBrandService {


    @Override
    public List<Brand> getPageList(String name) {
        return baseMapper.getPageList(name);
    }

    @Override
    public List<Brand> selectBrandAll() {
        return baseMapper.findAll();
    }
}
