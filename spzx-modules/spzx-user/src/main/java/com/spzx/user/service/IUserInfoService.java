package com.spzx.user.service;

import java.util.List;

import com.spzx.user.domain.UpdateUserLogin;
import com.spzx.user.domain.UserAddress;
import com.spzx.user.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 会员Service接口
 *
 * @author atguigu
 * @date 2024-09-27
 */
public interface IUserInfoService extends IService<UserInfo>
{

    /**
     * 查询会员列表
     *
     * @param userInfo 会员
     * @return 会员集合
     */
    public List<UserInfo> selectUserInfoList(UserInfo userInfo);

    /**
     * 查看用户的收货地址列表
     * @param userId 用户id
     * @return 地址列表
     */
    List<UserAddress> selectUserAddressList(Long userId);


    /**
     * 注册
     * @param userInfo
     */
    void register(UserInfo userInfo);

    UserInfo selectUserByUserName(String username);

    Boolean updateUserLogin(UpdateUserLogin updateUserLogin);
}
