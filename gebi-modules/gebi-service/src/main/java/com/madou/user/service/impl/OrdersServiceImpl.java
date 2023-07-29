package com.madou.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.madou.user.mapper.OrdersMapper;
import com.madou.user.api.model.entity.Orders;
import com.madou.user.service.OrdersService;
import org.springframework.stereotype.Service;

/**
* @author MA_dou
* @description 针对表【orders(充值订单表)】的数据库操作Service实现
* @createDate 2023-07-06 20:36:41
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




