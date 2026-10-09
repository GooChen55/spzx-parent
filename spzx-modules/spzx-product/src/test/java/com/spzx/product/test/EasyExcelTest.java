package com.spzx.product.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.spzx.common.core.utils.bean.BeanUtils;
import com.spzx.product.SpzxProductApplication;
import com.spzx.product.domain.Category;
import com.spzx.product.domain.vo.CategoryExcelVo;
import com.spzx.product.service.ICategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

//@SpringBootTest(classes = SpzxProductApplication.class)
@SpringBootTest
public class EasyExcelTest {

    @Autowired
    ICategoryService categoryService;

    //导出分类数据
    @Test
    public void testExport(){
        //1.获取导出数据
        List<Category> categoryAllList = categoryService.list();
        List<CategoryExcelVo> categoryExcelVoList = categoryAllList.stream().map(category -> {
            CategoryExcelVo vo = new CategoryExcelVo();
            BeanUtils.copyProperties(category, vo);
            return vo;
        }).toList();

        //2.利用EasyExcel组件提供的工具类导出数据。
        EasyExcel.write("D:/category.xlsx",CategoryExcelVo.class)
                .sheet("商品分类数据")
                .doWrite(categoryExcelVoList);
    }

    @Test
    public void testImport(){
        ExcelListener<CategoryExcelVo> readListener = new ExcelListener();
        EasyExcel.read("D:/category.xlsx")
                .head(CategoryExcelVo.class)
                .sheet()
                .registerReadListener(readListener)
                .doRead();
        List<CategoryExcelVo> datas = readListener.getDatas();
        for (CategoryExcelVo data : datas) {
            System.out.println("解析到的数据data = " + data);
        }
    }
}
