package com.spzx.user.service.impl;

import com.spzx.common.core.utils.HttpUtils;
import com.spzx.user.service.ISmsService;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl implements ISmsService {

    @Override
    public void send(String phone, String templateId, Map<String, Object> param) {
        String host = "https://gyyyx1.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "97c8ccac3fd5436a8e7ab5bbcc7c7498"; //购买短信服务，商家给用户分配的。可以设置appCode,获取AppKey：25620500     AppSecret：f0790fc1886f9fdcc81fe25d42988dc9

        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("templateId", templateId);
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("param", "**code**:"+param.get("code")+",**minute**:5");

        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println("发送短信成功:"+response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
