package com.spzx.product.test;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于解析Excel文件
 * @param <T>
 */
@Slf4j
public class ExcelListener<T> extends AnalysisEventListener<T> {

    List<T> datas = new ArrayList<>();

    /**
     * 每解析一行数据执行一次
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("ExcelListener - invoke ...");
        log.info(data.toString());
        datas.add(data);
    }

    /**
     * 所有的数据解析完成后执行一次当前方法。做收尾工作。
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("ExcelListener - doAfterAllAnalysed ...");
    }

    /**
     * 返回解析后的全部数据
     * @return 集合数据
     */
    public List<T> getDatas() {
        return datas;
    }
}
