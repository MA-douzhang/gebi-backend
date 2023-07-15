package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.model.entity.TextTask;
import com.yupi.springbootinit.service.TextTaskService;
import com.yupi.springbootinit.mapper.TextTaskMapper;
import org.springframework.stereotype.Service;

/**
* @author MA_dou
* @description 针对表【text_task(文本任务表)】的数据库操作Service实现
* @createDate 2023-07-12 20:32:15
*/
@Service
public class TextTaskServiceImpl extends ServiceImpl<TextTaskMapper, TextTask>
    implements TextTaskService{

}




