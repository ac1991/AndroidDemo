package com.wsq.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateRangeFormatter {
    public static void main(String[] args) {


        printRange("2023-11-24", null);
        printRange("2023-11-25", null);
        printRange("2023-11-21", null);
        printRange("2023-11-18", null);
        printRange("2023-11-17", null);
//        printRange();
    }

//    public static void printRange(LocalDate date, DateTimeFormatter formatter) {
//        LocalDate today = LocalDate.now(); // 获取当天日期
//        LocalDate tMinus3 = today.minusDays(3);
//        LocalDate tPlus3 = today.plusDays(3);
//
//        String range = "";
//        if (date.isBefore(tMinus3)) {
//            range = "历史";
//        } else if (date.isAfter(tPlus3)) {
//            range = "未来";
//        } else {
//            range = "最新";
//        }
//
//        System.out.println(date.format(formatter) + " 日期范围: " + range);
////        System.out.println("T-3到T+3日（最近七个自然日）为输出字符串为 " + range);
////        System.out.println("T+4及以后输出字符串为 " + range);
////        System.out.println("T-4及以前输出字符串为 " + range);
//    }

    public static void printRange(String date, DateTimeFormatter formatter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date data = sdf.parse(date);
            // 获取3天前和4天后的日期
            Calendar threeDaysAgo = Calendar.getInstance();
            threeDaysAgo.add(Calendar.DATE, - 4);

            Calendar fourDaysLater = Calendar.getInstance();
            fourDaysLater.add(Calendar.DATE, 3);

//            System.out.println("前三天：" + sdf.format(threeDaysAgo.getTime()));
//            System.out.println("后三天：" + sdf.format(fourDaysLater.getTime()));

            if(data.after(fourDaysLater.getTime())){
                System.out.println(date + "为未来");
                return;
            }

            if(data.before(threeDaysAgo.getTime())){
                System.out.println(date + "为历史");
                return;
            }

            System.out.println(date + "为最新");

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return;


    }


}