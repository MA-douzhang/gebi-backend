package com.madou.text.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.madou.text.api.model.entity.TextRecord;
import com.madou.text.mapper.TextRecordMapper;
import com.madou.text.service.TextRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author MA_dou
* @description 针对表【text_record(文本记录表)】的数据库操作Service实现
* @createDate 2023-07-12 20:32:09
*/
@Service
public class TextRecordServiceImpl extends ServiceImpl<TextRecordMapper, TextRecord>
    implements TextRecordService {

    @Override
    public String buildUserInput(TextRecord textRecord,String textTaskType) {
        String textContent = textRecord.getTextContent();
        //构造用户输入
        StringBuilder userInput = new StringBuilder();
        String gold = "请使用"+textTaskType+"语法对下面文章格式化";

        userInput.append(gold).append("\n");

        if (StringUtils.isNotBlank(textContent)) {
            textContent = textContent.trim();
            userInput.append(textContent);
        }
        return userInput.toString();
    }

}




