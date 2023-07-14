package com.yupi.springbootinit.manager;

import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.HashMap;

@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChat() {
        String input ="分析需求：\n" +
                "分析网站人数增长情况，请使用折线图\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,20\n" +
                "3号,30\n" +
                "4号,0\n" +
                "5号,130\n" +
                "6号,230\n" +
                "7号,20\n" +
                "8号,30\n"+
                "9号,20\n" +
                "10号,30\n";

        String chat = aiManager.doChat(input, 1659171950288818178L);
        System.out.println(chat);
//        String[] splits = chat.split("【【【【【");
//        if (splits.length < 3){
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 生成错误");
//        }
//        //todo 可以使用正则表达式保证数据准确性，防止中文出现
//        String genChart= splits[1].trim();
//        String genResult = splits[2].trim();
//        System.out.println("genChart: "+genChart);
//        System.out.println("genResult: "+genResult);
    }

    public static void main(String[] args) {
        //将非Json格式转化为json格式
        String res = "这个分析数据的图片代码" +
                "" +
                "{\n" +
                "  xAxis: {\n" +
                "    type: 'category',\n" +
                "    data: ['1号', '2号', '3号', '4号', '5号', '6号', '7号', '8号']\n" +
                "  },\n" +
                "  yAxis: {\n" +
                "    type: 'value'\n" +
                "  },\n" +
                "  series: [{\n" +
                "    data: [10, 20, 30, 0, 130, 230, 20, 30],\n" +
                "    type: 'line'\n" +
                "  }]\n" +
                "}";
        HashMap<String,Object> map1 = JSONUtil.toBean(res, HashMap.class);
        String toJsonStr = JSONUtil.toJsonStr(map1);
        System.out.println("str: "+toJsonStr);
    }
}
