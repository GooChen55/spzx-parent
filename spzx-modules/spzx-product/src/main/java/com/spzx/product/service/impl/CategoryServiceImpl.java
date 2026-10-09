package com.spzx.product.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spzx.common.core.utils.bean.BeanUtils;
import com.spzx.product.api.domain.vo.CategoryVo;
import com.spzx.product.domain.Category;
import com.spzx.product.domain.vo.CategoryExcelVo;
import com.spzx.product.helper.CategoryHelper;
import com.spzx.product.mapper.CategoryMapper;
import com.spzx.product.service.ICategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类Service业务层处理
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> treeSelect(Long id) {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId, id));
        //设置分类对象的hasChildren属性值，前端用于控制是否显示 > 箭头。
        //TODO 优化处理。
        categoryList.forEach(category -> {
            Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>().eq(Category::getParentId, category.getId()));
            if (count > 0) {
                category.setHasChildren(true);
            } else {
                category.setHasChildren(false);
            }
        });
        return categoryList;
    }

    @Override
    public List<Long> getAllCategoryIdList(Long categoryId) {
        //id顺序： 一级分类id、二级分类id、三级分类id
        List<Long> categoryIdList = new ArrayList<>();
        // 三级分类对象；二级分类对象；一级分类对象；
        List<Category> categoryList = this.getParentCategory(categoryId, new ArrayList<Category>());
        int size = categoryList.size();
        for (int i = size-1; i >= 0; i--) { //逆序操作
            Category category = categoryList.get(i);
            categoryIdList.add(category.getId());
        }
        return categoryIdList;
    }

    /**
     * 给定当前分类id,返回当前分类对象和祖先分类对象的集合
     * 利用递归算法：找祖先
     * 1.三级找二级、二级找一级 过程一样的。
     * 2.必须存在出口。一级找祖先没有就结束递归了。
     * 数量量不能太大，即递归次数不能太多。否则，容易出现栈溢出。
     *
     * @param categoryId   当前分类id
     * @param categoryList 用于存放  当前分类对象和祖先分类对象的集合
     * @return 当前分类对象和祖先分类对象的集合
     */
    private List<Category> getParentCategory(Long categoryId, List<Category> categoryList) {
        while (categoryId > 0) {
            Category category = categoryMapper.selectById(categoryId); //当前分类对象
            categoryList.add(category);
            return getParentCategory(category.getParentId(), categoryList);
        }
        return categoryList;  // 三级分类对象；二级分类对象；一级分类对象；
    }


    @Override
    public void exportData(HttpServletResponse response) throws Exception {
        //1.设置内容类型
        response.setContentType("application/vnd.ms-excel"); // 参考: tomcat/conf/web.xml文件  <mime-mapping>
        response.setCharacterEncoding("UTF-8"); //后端告诉前端，我给你返回的数据编码是UTF-8，前端需要也采用UTF-8编码打开文件。

        //2.设置响应头信息
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("分类数据", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx"); //告诉前端以附件形式打开。另存为。
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        //3.获取导出数据
        List<Category> categoryList = categoryMapper.selectList(null);
        List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>(categoryList.size());

        // 将从数据库中查询到的Category对象转换成CategoryExcelVo对象
        for(Category category : categoryList) {
            CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
            BeanUtils.copyProperties(category, categoryExcelVo, CategoryExcelVo.class);
            categoryExcelVoList.add(categoryExcelVo);
        }

        //4.通过若依提供的工具类实现导出。理解为文件下载。
        EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class).sheet("分类数据").doWrite(categoryExcelVoList);
    }


    @Override
    public void importData(MultipartFile file) throws Exception {
        List<CategoryExcelVo> categoryExcelVoList = EasyExcel.read(file.getInputStream()).head(CategoryExcelVo.class).sheet().doReadSync();
        if(!CollectionUtils.isEmpty(categoryExcelVoList)){
            List<Category> categoryList = new ArrayList<>(categoryExcelVoList.size());
            for (CategoryExcelVo categoryExcelVo : categoryExcelVoList) {
                Category category = new Category();
                BeanUtils.copyProperties(categoryExcelVo,category);
                categoryList.add(category);
            }
            this.saveBatch(categoryList);
        }
    }


    @Override
    public List<CategoryVo> getOneCategory() {
        //查询所有一级分类
        List<Category> oneCategoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>().eq(Category::getParentId, 0));
        //类型转换
        return oneCategoryList.stream().map((category) -> {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).toList();
    }

    @Override
    public List<CategoryVo> tree() {
        //categoryMapper.selectList(null);
        List<Category> allList = this.list();
        List<CategoryVo> allCategoryVolist = allList.stream().map((category) -> {
            CategoryVo vo = new CategoryVo();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).toList();
        //将列表数据转换为树结构,treeList存放都是一级分类，二级分类和三级分类都存放在children属性中。
        List<CategoryVo> treeList = CategoryHelper.buildTree(allCategoryVolist);
        return treeList;
    }
}