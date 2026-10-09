import request from '@/utils/request'

export function selectCategoryBrand(query){
    return request({
        url: '/product/categoryBrand/list',
        method: 'get',
        params: query
    })
}

export function getCategoryBrandById(id){
    return request({
        url: '/product/categoryBrand/'+id,
        method: 'get'
    })
}

export function addCategoryBrand(data){
    return request({
        url: '/product/categoryBrand',
        method: 'post',
        data: data
    })
}

export function updateCategoryBrand(data){
    return request({
        url: '/product/categoryBrand',
        method: 'put',
        data: data
    })
}

export function deleteCategoryBrandById(ids){
    return request({
        url: '/product/categoryBrand/'+ids,
        method: 'delete'
    })
}

//查询指定分类下的品牌列表
export function getBrandByCategoryId(id){
    return request({
        url: '/product/categoryBrand/brandList/'+id,
        method: 'get'
    })
}

export function getCategoryBrandAll(id){
    return request({
        url: '/product/categoryBrand/brandList/'+id,
        method: 'get'
    })
}