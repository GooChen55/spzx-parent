package com.spzx.cart.controller;

import com.spzx.cart.api.domain.CartInfo;
import com.spzx.cart.service.ICartService;
import com.spzx.common.core.domain.R;
import com.spzx.common.core.web.controller.BaseController;
import com.spzx.common.core.web.domain.AjaxResult;
import com.spzx.common.security.annotation.InnerAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "购物车接口")
@RestController
@RequestMapping
public class CartController extends BaseController {

    @Autowired
    private ICartService cartService;


    @Operation(summary = "添加商品到购物车")
    @GetMapping("/addToCart/{skuId}/{num}")
    public AjaxResult add2Cart(@PathVariable Long skuId,@PathVariable Integer num){
        //调用ICartService中添加购物车的方法
        cartService.add2Cart(skuId,num);
        return success();
    }

    @Operation(summary = "展示购物车")
    @GetMapping("/cartList")
    public AjaxResult cartList(){
        //调用ICartService中展示购物车的方法
        List<CartInfo> cartInfoList = cartService.cartList();
        return success(cartInfoList);
    }

    @Operation(summary = "更新购物项的选中状态")
    @GetMapping("/checkCart/{skuId}/{status}")
    public AjaxResult updateCartInfoStatus(@PathVariable Long skuId,@PathVariable Integer status){
        //调用ICartService中更新购物项的选中状态的方法
        cartService.updateCartIntoStatus(skuId,status);
        return success();
    }

    @Operation(summary = "购物车中购物项全选和全不选")
    @GetMapping("/allCheckCart/{status}")
    public AjaxResult allCheckCart(@PathVariable Integer status){
        //调用ICartService中全选全不选的方法
        cartService.allCheckCart(status);
        return success();
    }

    @Operation(summary = "删除购物项")
    @DeleteMapping("/deleteCart/{skuId}")
    public AjaxResult deleteCart(@PathVariable Long skuId){
        //调用ICartService中删除购物项的方法
        cartService.deleteCart(skuId);
        return success();
    }

    @Operation(summary = "清空购物车")
    @GetMapping("/clearCart")
    public AjaxResult clearCart(){
        //调用ICartService中清空购物车的方法
        cartService.clearCart();
        return success();
    }

    @InnerAuth
    @Operation(summary = "供微服务内部远程调用获取购物车中选中的购物项的方法")
    @GetMapping("/getCheckedCartInfo")
    public R<List<CartInfo>> getCheckedCartInfo(){
        //调用ICartService中获取已选中的购物项的方法
        List<CartInfo> cartInfoList = cartService.getCheckedCartInfo();
        return R.ok(cartInfoList);
    }

    @InnerAuth
    @Operation(summary = "供微服务内部远程调用清除选中的购物项的方法")
    @GetMapping("/clearCheckedCartInfo")
    public R<Void> clearCheckedCartInfo(){
        //调用ICartService中清楚选中的购物项的方法
        cartService.clearCheckedCartInfo();
        return R.ok();
    }
}