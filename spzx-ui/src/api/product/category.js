import request from '@/utils/request.js'

export function getTreeSelect(id){
    return request({
        url: `/product/category/getTreeSelect/${id}`,
        method: 'get'
    })
}