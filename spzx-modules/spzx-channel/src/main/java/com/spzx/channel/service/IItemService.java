package com.spzx.channel.service;

import com.spzx.channel.domain.ItemVo;

import java.util.concurrent.ExecutionException;

public interface IItemService {
    ItemVo item(Long skuId) throws Exception;
}