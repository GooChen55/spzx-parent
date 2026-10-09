import request from '@/utils/request'

// 查询订单列表
export function listInfo(query) {
    return request({
        url: '/order/orderInfo/list',
        method: 'get',
        params: query
    })
}

// 查询订单详细
export function getInfo(id) {
    return request({
        url: '/order/orderInfo/' + id,
        method: 'get'
    })
}