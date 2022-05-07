package com.zjh.utils;

import java.util.Scanner;

public class Utility {
    private static Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }

    public Utility() {

    }

    public static char readMenuSelection() {
        while (true) {
            String str = readKeyBoard(1, false);
            char c = str.charAt(0);
            if (c == 1 || c == 2 || c == 3 || c == 4 || c == 5) {
                return c;
            }

            System.out.print("选择错误，请重新输入：");
        }
    }

    public static char readChar() {
        String str = readKeyBoard(1, false);
        return str.charAt(0);
    }

    public static char readChar(char defaultValue){
        String str=readKeyBoard(1, true);
        return str.length()==0?defaultValue:str.charAt(0);
    }

    public static int readInt(){
        while (true){
            String str=readKeyBoard(2, false);
            try {
                int n=Integer.parseInt(str);
                return n;
            }catch (NumberFormatException var3){
                System.out.println("数字输入错误，请重新输入：");
            }
        }
    }

    public static int readInt(int defaultValue) {
        while(true) {
            String str = readKeyBoard(2, true);
            if (str.equals("")) {
                return defaultValue;
            }

            try {
                int n = Integer.parseInt(str);
                return n;
            } catch (NumberFormatException var4) {
                System.out.print("数字输入错误，请重新输入：");
            }
        }
    }

    /**输入限制长度的字符串*/
    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }

    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("") ? defaultValue : str;
    }

    public static char readConfirmSelection(){
        while (true){
            String str=readKeyBoard(1,false).toUpperCase();
            char c=str.charAt(0);
            if(c=='Y'||c=='N'){
                return c;
            }
            System.out.print("选择错误，请重新输入：");
        }
    }

    private static String readKeyBoard(int limit, boolean blankReturn){
        String line="";

        while (scanner.hasNextLine()){
            line=scanner.nextLine();
            if(line.length()==0){
                if(blankReturn){
                    return line;
                }
            }else {
                if(line.length()>=1 && line.length()<=limit){
                    break;
                }
                System.out.println("输入长度（不大于" + limit + "）错误，请重新输入：");
            }
        }
        return line;
    }
}

