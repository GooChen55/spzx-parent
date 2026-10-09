package com.spzx.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spzx.product.api.domain.vo.CategoryVo;
import com.spzx.product.domain.Category;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 商品分类Service接口
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 获取分类下拉树列表
     * @param id
     * @return
     */
    public List<Category> treeSelect(Long id);


    /**
     * 根据指定分类id,返回当前分类id以及它的祖先id
     * @param categoryId 当前分类id
     * @return 当前及祖先分类id
     *  例如：三级分类id = 3
     *      一级分类id = 1
     *      二级分类id = 2
     *
     *      给定二级分类id=3 返回[1,2,3]
     */
    List<Long> getAllCategoryIdList(Long categoryId);

    /**
     * 导出分类
     * @param response
     */
    void exportData(HttpServletResponse response) throws Exception;

    /**
     * 导入分类：将上传Excel进行解析，将数据保存到数据库中。
     * @param file 上传Excel
     */
    void importData(MultipartFile file) throws Exception;

    /**
     * 查询所有一级分类
     * @return 一级分类集合
     */
    List<CategoryVo> getOneCategory();

    /**
     * 查询所有三级分类，树结构
     * @return
     */
    List<CategoryVo> tree();
}