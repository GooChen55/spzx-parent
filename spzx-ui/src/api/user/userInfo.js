import request from '@/utils/request'

// 查询会员列表
export function listUserInfo(query) {
  return request({
    url: '/user/userInfo/list',
    method: 'get',
    params: query
  })
}

// 查询会员详细
export function getUserInfo(id) {
  return request({
    url: '/user/userInfo/' + id,
    method: 'get'
  })
}

// 根据用户id查询收货地址列表
export function getUserAddress(userId) {
  return request({
    url: '/user/userInfo/getUserAddress/' + userId,
    method: 'get'
  })
}