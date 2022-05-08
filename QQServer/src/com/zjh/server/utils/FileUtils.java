package com.zjh.server.utils;

import java.io.*;

/**
 * @author 张俊鸿
 * @description: 文件工具读取到内存和保存到本地类
 * @since 2022-05-08 15:14
 */
public class FileUtils {

    /**
     * 读取文件到内存
     * @param src 文件的本地路径
     * @return {@link byte[]}
     */
    public static byte[] readFile(String src){
        BufferedInputStream bis = null;
        try {
            new BufferedInputStream(new FileInputStream(src));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //创建输出流对象
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        //字节数组实际长度
        int len = 0;
        try {
            while ((len = bis.read(b)) !=-1 ){
                bos.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] array = bos.toByteArray();
        try {
            bos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * 存储文件到本地
     *
     * @param fileBytes 文件字节数组
     * @param desc      目的路径
     */
    public static void storeFile(byte[] fileBytes, String desc){
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(desc));
            bos.write(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
