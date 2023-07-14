package com.yupi.springbootinit.utils;

import org.springframework.util.ResourceUtils;

import java.io.*;

public class TxtUtilsTest {
    public static void main(String[] args) {
        try {
            File file = ResourceUtils.getFile("classpath:笔记.txt");
            readerFile(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void readerFile(File file) {
        try {
            // 获取文件内容
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                // 处理文件内容，例如输出到控制台
                if (builder.length()+line.length()<=980){
                    builder.append(line).append("\n");
                }else {
                    //保存数据库


                    System.out.println(builder);
                    System.out.println("长度为"+builder.length());
                    System.out.println("=========================");
                    builder.delete(0,builder.length());
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
