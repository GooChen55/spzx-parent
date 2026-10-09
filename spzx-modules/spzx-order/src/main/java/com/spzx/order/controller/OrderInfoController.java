package com.spzx.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.spzx.common.core.domain.R;
import com.spzx.common.core.utils.poi.ExcelUtil;
import com.spzx.common.core.web.controller.BaseController;
import com.spzx.common.core.web.domain.AjaxResult;
import com.spzx.common.core.web.page.TableDataInfo;
import com.spzx.common.log.annotation.Log;
import com.spzx.common.log.enums.BusinessType;
import com.spzx.common.security.annotation.InnerAuth;
import com.spzx.common.security.annotation.RequiresLogin;
import com.spzx.common.security.annotation.RequiresPermissions;
import com.spzx.order.api.domain.OrderInfo;
import com.spzx.order.domain.vo.OrderForm;
import com.spzx.order.domain.vo.TradeVo;
import com.spzx.order.mapper.OrderInfoMapper;
import com.spzx.order.service.IOrderInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Tag(name = "订单接口管理")
@RestController
@RequestMapping("/orderInfo")
public class OrderInfoController extends BaseController {
    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;


    @Operation(summary = "取消订单")
    @GetMapping("/cancelOrder/{orderId}")
    public AjaxResult cancelOrder(@PathVariable Long orderId){
        //调用IOrderService中取消订单的方法
        orderInfoService.cancelOrder(orderId,1);
        return success();
    }


    @Operation(summary = "分页查询我的订单")
    @GetMapping("/userOrderInfoList/{pageNum}/{pageSize}")
    public TableDataInfo getMyOrderInfoList(@PathVariable Integer pageNum,@PathVariable Integer pageSize, String orderStatus){
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        //调用IOrderInfoService中分页及带条件查询的方法
        List<OrderInfo> orderInfoList = orderInfoService.getMyOrderInfoList(orderStatus);
        return getDataTable(orderInfoList);
    }

    @Operation(summary = "根据订单id查询订单信息")
    @GetMapping("/getOrderInfo/{orderId}")
    public AjaxResult getOrderInfo(@PathVariable Long orderId){
        //调用getInfo方法
        OrderInfo byId = orderInfoService.getById(orderId);
        return success(byId);
    }

    @Operation(summary = "提交订单")
    @PostMapping("/submitOrder")
    public AjaxResult submitOrder(@RequestBody OrderForm orderForm){
        //调用IOrderInfoService中提交订单的方法
        Long orderId = orderInfoService.submitOrder(orderForm);
        return success(orderId);
    }

    @Operation(summary = "立即购买")
    @GetMapping("/buy/{skuId}")
    public AjaxResult buy(@PathVariable Long skuId){
        //调用IOrderService中立即购买的方法
        TradeVo tradeVo = orderInfoService.buy(skuId);
        return success(tradeVo);
    }

    @Operation(summary = "去结算")
    @GetMapping("/trade")
    public AjaxResult trade(){
        //调用IOrderInfoService中去结算的方法
        TradeVo tradeVo = orderInfoService.trade();
        return success(tradeVo);
    }

    /**
     * 查询订单列表
     */
    @Operation(summary = "查询订单列表")
    @RequiresPermissions("user:orderInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(OrderInfo orderInfo) {
        startPage();
        List<OrderInfo> list = orderInfoService.selectOrderInfoList(orderInfo);
        return getDataTable(list);
    }

    /**
     * 导出订单列表
     */
    @Operation(summary = "导出订单列表")
    @RequiresPermissions("user:orderInfo:export")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderInfo orderInfo) {
        List<OrderInfo> list = orderInfoService.selectOrderInfoList(orderInfo);
        ExcelUtil<OrderInfo> util = new ExcelUtil<OrderInfo>(OrderInfo.class);
        util.exportExcel(response, list, "订单数据");
    }

    /**
     * 获取订单详细信息
     */
    @Operation(summary = "获取订单详细信息")
    @RequiresPermissions("user:orderInfo:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(orderInfoService.selectOrderInfoById(id));
    }


    @Operation(summary = "供微服务内部远程调用根据订单号查询订单信息的方法")
    @GetMapping("/getOrderInfoByOrderNo/{orderNo}")
    public R<OrderInfo> getOrderInfoByOrderNo(@PathVariable String orderNo){
        //调用IOrderService中根据订单号查询订单信息的方法
        OrderInfo orderInfo = orderInfoService.getOrderInfoByOrderNo(orderNo);
        return R.ok(orderInfo);
    }

}