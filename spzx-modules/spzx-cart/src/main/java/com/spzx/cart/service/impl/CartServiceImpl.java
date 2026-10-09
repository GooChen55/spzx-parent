package com.spzx.cart.service.impl;

import com.spzx.cart.api.domain.CartInfo;
import com.spzx.cart.service.ICartService;
import com.spzx.common.core.constant.SecurityConstants;
import com.spzx.common.core.context.SecurityContextHolder;
import com.spzx.common.core.domain.R;
import com.spzx.common.core.exception.ServiceException;
import com.spzx.product.api.RemoteProductService;
import com.spzx.product.api.domain.ProductSku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private RemoteProductService remoteProductService;

    //获取Redis中购物车中的key的方法
    public String getCartKey(){
        //获取用户id
        Long userId = SecurityContextHolder.getUserId();
        return "user:cart:"+userId;
    }
    @Override
    public void add2Cart(Long skuId, Integer num) {
        //获取购物车的key
        String cartKey = getCartKey();
        //从购物车中获取当前商品对应的购物项
        CartInfo cartInfo = (CartInfo) redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));
        //判断是否为null
        if(null != cartInfo){
            //之前已经向购物车中添加过该商品，只需要将购物车中数量设置为之前的数量加上传入过来的数量
            cartInfo.setSkuNum(Math.min((cartInfo.getSkuNum() + num), 99));
        }else{
            //证明是第一次添加该商品到购物车
            cartInfo = new CartInfo();
            //设置商品数量为1
            cartInfo.setSkuNum(1);
            //设置商品SkuId
            cartInfo.setSkuId(skuId);
            //设置添加购物车的日期
            cartInfo.setCreateTime(new Date());
            //设置添加购物车的用户的名字
            cartInfo.setCreateBy(SecurityContextHolder.getUserName());
            //远程调用商品微服务根据商品skuId查询商品Sku信息
            R<ProductSku> productSku = remoteProductService.getProductSku(skuId, SecurityConstants.INNER);
            //判断是否有异常
            if(R.FAIL == productSku.getCode()){
                //远程调用失败
                throw new ServiceException(productSku.getMsg());
            }
            //获取商品Sku信息
            ProductSku productSku1 = productSku.getData();
            //给购物项设置商品Sku信息
            cartInfo.setSkuName(productSku1.getSkuName());
            cartInfo.setThumbImg(productSku1.getThumbImg());
            //设置放入购物车时的价格
            cartInfo.setCartPrice(productSku1.getSalePrice());
            //设置商品Sku的实时价格
            cartInfo.setSkuPrice(productSku1.getSalePrice());
        }
        //将购物车放入Redis中
        redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId),cartInfo);
    }


    @Override
    public List<CartInfo> cartList() {
        //获取购物车的key
        String cartKey = getCartKey();
        //获取购物车中所有的购物项
        List<CartInfo> cartInfoList = redisTemplate.opsForHash().values(cartKey);
        //遍历所有的购物项
        cartInfoList.forEach(cartInfo -> {
            //根据SkuId远程调用商品微服务获取最新的商品Sku售价
            R<ProductSku> productSkuR = remoteProductService.getProductSku(cartInfo.getSkuId(), SecurityConstants.INNER);
            if(R.FAIL == productSkuR.getCode()){
                throw new ServiceException(productSkuR.getMsg());
            }
            //获取商品Sku
            ProductSku productSku = productSkuR.getData();
            //获取最新的售价
            BigDecimal salePrice = productSku.getSalePrice();
            //更新CartInfo中的实时价格
            cartInfo.setSkuPrice(salePrice);
            //将更新过实时价格的购物项放入Redis中
            redisTemplate.opsForHash().put(cartKey,String.valueOf(cartInfo.getSkuId()),cartInfo);
        });
        return cartInfoList;
    }

    @Override
    public void updateCartIntoStatus(Long skuId, Integer status) {
        //获取购物车的key
        String cartKey = getCartKey();
        //获取购物车中的当前的购物项
        CartInfo cartInfo = (CartInfo) redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));
        //设置购物项的选中状态
        cartInfo.setIsChecked(status);
        //重新放回Redis中
        redisTemplate.opsForHash().put(cartKey,String.valueOf(skuId),cartInfo);
    }

    @Override
    public void allCheckCart(Integer status) {
        //获取购物车的key
        String cartKey = getCartKey();
        //获取所有的购物项
        List<CartInfo> cartInfoList = redisTemplate.opsForHash().values(cartKey);
        //遍历
        cartInfoList.forEach(cartInfo -> {
            //设置每一个每一个购物项的选中状态
            cartInfo.setIsChecked(status);
            //重新放入Redis
            redisTemplate.opsForHash().put(cartKey,String.valueOf(cartInfo.getSkuId()),cartInfo);
        });
    }

    @Override
    public void deleteCart(Long skuId) {
        //获取购物车的key
        String cartKey = getCartKey();
        //删除购物项
        redisTemplate.opsForHash().delete(cartKey,String.valueOf(skuId));
    }

    @Override
    public void clearCart() {
        //清空Redis中的购物车
        redisTemplate.delete(getCartKey());
    }

    @Override
    public List<CartInfo> getCheckedCartInfo() {
        //获取购物车的key
        String cartKey = getCartKey();
        //获取所有的购物项
        List<CartInfo> cartInfoList = redisTemplate.opsForHash().values(cartKey);
        //获取选中的购物项
        List<CartInfo> checkedCartInfoList = cartInfoList.stream().filter(cartInfo -> cartInfo.getIsChecked() == 1).collect(Collectors.toList());
        return checkedCartInfoList;
    }

    @Override
    public void clearCheckedCartInfo() {
        //获取购物车的key
        String cartKey = getCartKey();
        //获取所有选中的购物项
        List<CartInfo> checkedCartInfo = getCheckedCartInfo();
        //从Redis中删除选中的购物项
        checkedCartInfo.forEach(cartInfo -> {
            redisTemplate.opsForHash().delete(cartKey,String.valueOf(cartInfo.getSkuId()));
        });
    }
}