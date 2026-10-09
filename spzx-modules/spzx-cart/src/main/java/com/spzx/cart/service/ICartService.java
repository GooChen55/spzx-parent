package com.spzx.cart.service;

import com.spzx.cart.api.domain.CartInfo;

import java.util.List;

public interface ICartService {

    /**
     * 添加购物车
     * @param skuId
     * @param num
     */
    void add2Cart(Long skuId, Integer num);

    /**
     * 展示购物车
     * @return
     */
    List<CartInfo> cartList();

    /**
     * 更新购物项的选中状态
     * @param skuId
     * @param status
     */
    void updateCartIntoStatus(Long skuId, Integer status);

    /**
     * 购物车中购物项全选全不选
     * @param status
     */
    void allCheckCart(Integer status);

    /**
     * 删除购物项
     * @param skuId
     */
    void deleteCart(Long skuId);

    /**
     * 清空购物车
     */
    void clearCart();

    /**
     * 获取选中的购物项的方法
     * @return
     */
    List<CartInfo> getCheckedCartInfo();

    /**
     * 清空选中的购物项的方法
     */
    void clearCheckedCartInfo();
}
