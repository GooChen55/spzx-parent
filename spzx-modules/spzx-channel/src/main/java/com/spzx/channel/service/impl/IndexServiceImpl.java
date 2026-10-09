package com.spzx.channel.service.impl;

import com.spzx.channel.service.IIndexService;
import com.spzx.common.core.constant.SecurityConstants;
import com.spzx.common.core.domain.R;
import com.spzx.common.core.exception.ServiceException;
import com.spzx.product.api.RemoteCategoryService;
import com.spzx.product.api.RemoteProductService;
import com.spzx.product.api.domain.ProductSku;
import com.spzx.product.api.domain.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IIndexService {

    @Autowired
    RemoteCategoryService remoteCategoryService;

    @Autowired
    RemoteProductService remoteProductService;

    @Override
    public Map<String, Object> index() {
        //1.获取一级分类
        R<List<CategoryVo>> oneCategoryListResult = remoteCategoryService.getOneCategory(SecurityConstants.INNER);
        if(R.FAIL == oneCategoryListResult.getCode()){
            throw new ServiceException(oneCategoryListResult.getMsg());
        }

        //2.获取畅销商品
        R<List<ProductSku>> topSaleListResult = remoteProductService.getTopSale(SecurityConstants.INNER);
        if(R.FAIL == topSaleListResult.getCode()){
            throw new ServiceException(topSaleListResult.getMsg());
        }

        //3.组装成Map,key名称必须与接口文档一致
        Map<String, Object> map = new HashMap<>();
        map.put("categoryList",oneCategoryListResult.getData());
        map.put("productSkuList",topSaleListResult.getData());
        //4.返回Map
        return map;
    }
}
