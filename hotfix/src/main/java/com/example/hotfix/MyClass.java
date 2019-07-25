package com.example.hotfix;

public class MyClass {
    public static void main(String[] args){
        System.out.println(func("Tencent"));
        System.out.println(((((0xF0FF & 0X000F)|0X00F0)<<1)/(4>>1)) + "");

    }

    public static String func(String ss){
        System.out.println("func ss.substring(1)" + (ss.length() > 0 ? func(ss.substring(1)) + ss.charAt(0) : ""));
        return ss.length() > 0 ? func(ss.substring(1)) + ss.charAt(0) : "";
    }
}
