package com.spzx.product.api.domain.vo;

import lombok.Data;

@Data
public class SkuLockVo {

    private Long skuId;

    private Integer skuNum;

    /**
     * 是否有库存
     **/
    private Boolean isHaveStock = false;

}