package com.yupi.springbootinit.manager;

import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;

public class AiManagerTestMarkDown {

    public static void main(String[] args) {
        String accessKey = "ux7bpq5mqr8db3n0dfhd46bunkebr8f3";
        String secretKey = "eg6zsakkz0av0f6jae3g3cw163nw56hc";
        YuCongMingClient yuCongMingClient = new YuCongMingClient(accessKey, secretKey);
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1666258198769700865L);

        String dataText ="          log.warn(\\\"订单未支付成功,重新放回队列,订单号为\\\"+order.getId());            channel.basicNack(deliveryTag,false,true);        }else {            //消息确认            channel.basicAck(deliveryTag,false);        }    }注意rabbitmq的重发机制不能在try-catch中使用，否则会不生效，因为catch也会算一次确定消息。";
        System.out.println(dataText.length());
        String trim = dataText.trim();
//        String replace = trim.replaceAll("\\s*|\r|\n|\t", "");
//       replace="请使用markdown语法对下面文章格式化，并生成markdown语法"+"\n"+replace;
//        System.out.println(replace);
        trim="请使用markdown语法对下面文章格式化，并生成markdown语法"+"\n"+trim;
        devChatRequest.setMessage(trim);
        BaseResponse<DevChatResponse> devChatResponseBaseResponse = yuCongMingClient.doChat(devChatRequest);
        System.out.println(devChatResponseBaseResponse);
        DevChatResponse data = (DevChatResponse)devChatResponseBaseResponse.getData();
        if (data != null) {
            String content = data.getContent();
            System.out.println(content);
        }

    }
}
