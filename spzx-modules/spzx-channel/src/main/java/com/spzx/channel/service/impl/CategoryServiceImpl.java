package com.spzx.channel.service.impl;

import com.spzx.channel.service.ICategoryService;
import com.spzx.common.core.constant.SecurityConstants;
import com.spzx.common.core.domain.R;
import com.spzx.common.core.exception.ServiceException;
import com.spzx.product.api.RemoteCategoryService;
import com.spzx.product.api.domain.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    
    @Autowired
    RemoteCategoryService remoteCategoryService;
    
    @Override
    public List<CategoryVo> tree() {
        R<List<CategoryVo>> treeListResult = remoteCategoryService.tree(SecurityConstants.INNER);
        if(R.FAIL == treeListResult.getCode()){
            throw new ServiceException(treeListResult.getMsg());
        }
        return treeListResult.getData();
    }
}
