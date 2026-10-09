import request from "@/utils/request.js"; //对Axios进行封装。用于发起ajax请求。简化异步开发。

//前端和后端接口进行对接：
export function listBrand(query){ //   {name: '小米'}
    return request({
        //            /dev-api/product/brand/list
        url: '/product/brand/list',  //   /product/brand/list?name=小米
        method: 'get',
        params: query  // {name: '小米'}
    })
}

export function addBrand(data){
    return request({
        url: '/product/brand',
        method: 'post',
        data  //    属性名称和参数名称一致的话，可以进行属性简写   data : data    =>   data
    })
}

export function getBrand(id){
    return request({
        url: `/product/brand/${id}`, //模板字符串
        method: 'get'
    })
}

export function updateBrand(data){
    return request({
        url: '/product/brand',
        method: 'put',
        data  //    属性名称和参数名称一致的话，可以进行属性简写   data : data    =>   data
    })
}

export function deleteBrand(ids){
    return request({
        url: `/product/brand/${ids}`, //模板字符串
        method: 'delete'
    })
}

export function getBrandAll(){
    return request({
        url: '/product/brand/getBrandAll',
        method: 'get'
    })
}