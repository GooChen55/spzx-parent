import request from '@/utils/request.js'

export function getTreeSelect(parentCode) {
    return request({
        url: '/user/region/treeSelect/' + parentCode,
        method: 'get'
    })
}