package com.example.demo;

import com.example.demo.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author wuyuhan
 * @date 2023/4/29 21:29
 */

@SpringBootTest
@ContextConfiguration(classes = Demo3Application.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;


    @Test
    void sendMail() {
        mailClient.sendMail("1824162728@qq.com", "test", "welcome, Mail !");

    }

    @Test
    void testThymeMail() {
        Context context = new Context();
        context.setVariable("username", "wuyuhan");
        String process = templateEngine.process("/mail/demo", context);
        System.out.println(process);
        mailClient.sendMail("1824162728@qq.com", "test", process);
    }
}
