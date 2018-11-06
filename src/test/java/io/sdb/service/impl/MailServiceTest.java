package io.sdb.service.impl;

import cn.hutool.extra.mail.Mail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MailServiceTest {

    @Autowired
    MailService mailService;

    @Test
    public void sendMail() {
        mailService.sendMail("我的邮件", "你好", "406123228@qq.com");
    }
}