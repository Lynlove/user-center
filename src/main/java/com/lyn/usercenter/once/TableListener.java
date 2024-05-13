package com.lyn.usercenter.once;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class TableListener implements ReadListener<XingQiuTableUserInfo> {
/**
* 这个每一条数据解析都会来调用
*
* @param data one row value. Is is same as {@link
AnalysisContext#readRowHolder()}
* @param context
*/
@Override
public void invoke(XingQiuTableUserInfo data, AnalysisContext context) {
System.out.println(data);
}
/**
* 所有数据解析完成了 都会来调用
*
* @param context
创建一个excel表格，数据包括成员编号、用户昵称，这两个字段与XingQiuTableUserInfo中@ExcelProperty的
value保持一致
*/
@Override
public void doAfterAllAnalysed(AnalysisContext context) {
System.out.println("所有数据解析完成！");
}
}
