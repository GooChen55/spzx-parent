package com.spzx.user.service;

import java.util.List;
import com.spzx.user.domain.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户地址Service接口
 *
 * @author atguigu
 * @date 2024-09-27
 */
public interface IUserAddressService extends IService<UserAddress>
{

    /**
     * 查询用户地址列表
     *
     * @return 用户地址集合
     */
    public List<UserAddress> selectUserAddressList();

    /**
     * 新增用户地址
     *
     * @param userAddress 用户地址
     * @return 结果
     */
    public int insertUserAddress(UserAddress userAddress);

    /**
     * 修改用户地址
     *
     * @param userAddress 用户地址
     * @return 结果
     */
    public int updateUserAddress(UserAddress userAddress);
}
