package com.yupi.springbootinit.utils;

import com.alibaba.excel.util.FileUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.ArrayList;

/**
 * 读取文本工具栏
 */
public class TxtUtils {
    public static void main(String[] args) {
        try {
            File file = ResourceUtils.getFile("classpath:笔记.txt");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String> readerFile(MultipartFile file) {
        ArrayList<String> list= new ArrayList<>();
        try {
            // 获取文件内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            StringBuilder builder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // 处理文件内容，例如输出到控制台
                if (builder.length()+line.length()<920){
                    builder.append(line);
                }else {
                    //保存数据库
                    list.add(builder.toString());
                    builder.delete(0,builder.length());
                }
                //保证数据不丢失
                if (builder.length()==0){
                    builder.append(line);
                }
            }
            //数据可能不超过990
            list.add(builder.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
