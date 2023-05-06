package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.ActivationStatus;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/5/1 21:24
 */
@Controller
public class LoginController implements ActivationStatus {
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String CONTEXT_PATH;

    @Autowired
    private Producer kaptchaProducer;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    /**
     * 链接可以一致，但必须请求方式不同
     * @return
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginInPage() {
        // 实际跳转的url
        return "/site/login";
    }


    @RequestMapping(path="/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> register = userService.register(user);
        if (register == null || register.isEmpty()) {
            // success
//            System.out.println("success....");
            model.addAttribute("msg", "注册成功，我们已经向您发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", register.get("usernameMsg"));
            model.addAttribute("passwordMsg", register.get("passwordMsg"));
            model.addAttribute("emialMsg", register.get("emialMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,
                                          @PathVariable("code") String code) {
        int activation = userService.activation(userId, code);
        if (activation == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，即将前往登录页！");
            model.addAttribute("target", "/login");
        } else if (activation == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "重复激活，即将前往主页！");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，即将前往注册页！");
            model.addAttribute("target", "/register");
        }
        // 前往临时页
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 验证码存入session
        session.setAttribute("kaptcha", text);

        // 将图片传输给浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("获取验证码图片错误！" + e.getMessage());
//            throw new RuntimeException(e);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, String username, String password, String code,
                        boolean rememberMe, HttpSession session, HttpServletResponse response) {
        // 校验 验证码
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (!kaptcha.equalsIgnoreCase(code) || StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code)) {
            model.addAttribute("codeMsg","验证码错误！");
            return "/site/login";
        }

        int expiredSeconds = rememberMe ? REMEBER_EXPIRED_SECONDS: DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> loginInfo = userService.login(username, password, expiredSeconds);
        if (loginInfo.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", loginInfo.get("ticket").toString());
            cookie.setMaxAge(expiredSeconds);
            cookie.setPath(CONTEXT_PATH);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", loginInfo.get("usernameMsg"));
            model.addAttribute("passwordMsg", loginInfo.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/index";
    }
}
