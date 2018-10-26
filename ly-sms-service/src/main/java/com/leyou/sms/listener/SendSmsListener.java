package com.leyou.sms.listener;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 21 17:02
 **/
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SendSmsListener {

    @Autowired
    private SmsProperties props;

    @Autowired
    private SmsUtils smsUtils;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.sms.queue",durable = "true"),
            exchange = @Exchange(value = "ly.sms.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "sms.verify.code"))
    private void listenSms(Map<String,String> msg) throws Exception {
        // 如果没有参数，不要处理
        if (msg == null || msg.size() <= 0) {
            return;
        }
        String code = msg.get("code");
        String phone = msg.get("phone");
        // 如果传递过来的手机号和验证码是空的，放弃处理
        if (StringUtils.isBlank(code) || StringUtils.isBlank(phone)) {
            return;
        }
        // 发送成功
        SendSmsResponse resp = this.smsUtils.sendSms(phone, code, props.getSignName(), props.getVerifyCodeTemplate());

    }

}
