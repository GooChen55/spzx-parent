package com.spzx.product.helper;

import com.spzx.product.api.domain.vo.CategoryVo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 辅助工具类：
 */
public class CategoryHelper {
    /**
     * 转换成树结构
     * @param allCategoryVolist 分类列表集合
     * @return 树结构分类集合(一级)
     */
    public static List<CategoryVo> buildTree(List<CategoryVo> allCategoryVolist) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(allCategoryVolist)){
            for (CategoryVo categoryVo : allCategoryVolist) {
                if(categoryVo.getParentId().intValue() == 0){ //查找所有一级分类
                    categoryVo = findChildren(categoryVo,allCategoryVolist); //找孩子，返回自己
                    categoryVoList.add(categoryVo);
                }
            }
        }
        return categoryVoList;
    }

    /**
     * 利用递归算法：找孩子
     *      要求：
     *          1.同类问题可以使用递归。一级找二级、二级找三级
     *          2.递归出口。三级找四级，就会退出递归。因为我们分类数据只有三级。
     * @param categoryVo 当前分类
     * @param allCategoryVolist 所有分类（含有当前分类的孩子数据）
     * @return 当前分类
     */
    private static CategoryVo findChildren(CategoryVo categoryVo, List<CategoryVo> allCategoryVolist) {
        categoryVo.setChildren(new ArrayList<>()); //给children属性初始化。避免空指针。

        for (CategoryVo vo : allCategoryVolist) {
            // -128 ~ 127  常量池
            // 大于127比较是两个包装类型对象地址。注意！！！
            if(categoryVo.getId().intValue() == vo.getParentId().intValue()){ //分类父id正好是当前分类的id,那它就是当前分类的孩子
                vo = findChildren(vo,allCategoryVolist); //孩子再找孩子
                categoryVo.getChildren().add(vo);
            }
        }

        return categoryVo;
    }
}
