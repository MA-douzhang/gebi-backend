package com.yupi.springbootinit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChartMapperTest {

    @Resource
    ChartMapper chartMapper;

    @Test
    void queryChartData() {
        long chartId=1663172101098369026L;
        String sql = String.format("select * from chart_%s",chartId);
        List<Map<String, Object>> maps = chartMapper.queryChartData(sql);
        System.out.println(maps);

    }
}
