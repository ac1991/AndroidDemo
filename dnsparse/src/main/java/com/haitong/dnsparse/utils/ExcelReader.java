package com.haitong.dnsparse.utils;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelReader {
    public static List<String>  readerUrlFromExcel(Context context, String fileName){
        List<String> domainList = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open(fileName); // 替换为你的Excel文件路径
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0); // 获取第一个工作表
            int numRows = sheet.getRows(); // 获取行数
            for (int row = 0; row < numRows; row++) {
                //读取第n行，第0列
                Cell cell = sheet.getCell(0, row);
                domainList.add(cell.getContents());
            }
            workbook.close();
            inputStream.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }
        return domainList;
    }

    public static void writeExcel(Context context, ArrayMap<String, List<String>> datas){
        try {
            Log.e("缓存路径", context.getCacheDir().getAbsolutePath());
            WritableWorkbook workbook = Workbook.createWorkbook(new File(context.getCacheDir().getAbsolutePath() + "/域名.xls"));
            WritableSheet sheet = workbook.createSheet("sheet0", 0);

            int rowIndex = 0;
            for (int i = 0; i < datas.size(); i++){
                String domain = datas.keyAt(i);
                List<String> ipList = datas.get(domain);
//                Log.e("域名解析", domain);
                Label label = new Label(0, rowIndex, domain);
                sheet.addCell(label);
                if (ipList.isEmpty()){
                    rowIndex++;
                }
                for (String ip: ipList){
//                    Log.d("域名解析", "ip:" + ip);
                    Label ipLabel = new Label(1, rowIndex, ip);
                    sheet.addCell(ipLabel);
                    rowIndex++;
                }
            }
            workbook.write();
            workbook.close();


        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
    }
}
