package com.leyou.smstest;

import com.aliyuncs.exceptions.ClientException;
import com.leyou.LySmsService;
import com.leyou.sms.config.SmsProperties;
import com.leyou.sms.utils.SmsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 21 20:08
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySmsService.class)
public class SendSmsTest {

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties props;

    @Test
    public void testSend() throws ClientException {
        String signName = props.getSignName();
        String template = props.getVerifyCodeTemplate();
        this.smsUtils.sendSms("18115401712","123456",signName,template);
    }
}
