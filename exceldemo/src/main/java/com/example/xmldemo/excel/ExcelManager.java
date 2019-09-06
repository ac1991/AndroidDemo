package com.example.xmldemo.excel;

import android.app.Application;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellReferenceHelper;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelManager {
    public void readExl(Application application){
        // 字符列表
        List<String> list = new ArrayList<String>();
        // 文件路径
        String filePath = "E:/test.xls";
        // 输入流
        InputStream is = null;
        // Excel工作簿
        Workbook workbook = null;

        try {
            // 加载Excel文件
            is = application.getResources().getAssets().open("demo.xls");//new FileInputStream(filePath);
            // 获取workbook
            workbook = Workbook.getWorkbook(is);
        } catch (Exception e) {}

        // 获取sheet, 如果你的workbook里有多个sheet可以利用workbook.getSheets()方法来得到所有的
        Sheet sheet = workbook.getSheet(0);// 这里只取得第一个sheet的值，默认从0开始
        System.out.println(sheet.getColumns());// 查看sheet的列
        System.out.println(sheet.getRows());// 查看sheet的行

        Cell cell0 = null;// 单个单元格
        // 开始循环，取得cell里的内容，按具体类型来取
        // 这里只取String类型
        for (int j = 0;j<sheet.getRows() - 1;j++){
            StringBuffer sb = new StringBuffer();
//            for (int i=0;i<sheet.getColumns() - 1;i++){
                // 列,行
                cell0 = sheet.getCell(0, j);
                sheet.getCell(CellReferenceHelper.getColumn(""), j);
                sb.append(cell0.getContents());// 获取单元格内容
                sb.append(",");// 将单元格的每行内容用逗号隔开

                Cell cell1 = sheet.getCell(1, j);
                 sb.append(cell1.getContents());// 获取单元格内容
//            }
            list.add(sb.toString());//将每行的字符串用一个String类型的集合保存。
        }

        workbook.close();// 关闭工作簿

        // 迭代集合查看每行的数据
        for (String ss : list) {
            System.out.println(ss);

        }
    }
}
