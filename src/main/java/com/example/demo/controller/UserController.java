package com.example.demo.controller;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.CommunityUtil;
import com.example.demo.util.HostHolderUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author wuyuhan
 * @date 2023/5/6 15:44
 */
@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    // 获取当前用户
    @Autowired
    private HostHolderUtil hostHolderUtil;

    @Autowired
    private UserService userService;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String upload(Model model, MultipartFile headerImage) {
        if (headerImage == null) {
            model.addAttribute("error", "图片上传为空！");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();
        // 获取图片格式
        String suffixName = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffixName)) {
            model.addAttribute("error", "你还没有选择图片");
            return "/site/setting";
        }
        filename = CommunityUtil.generateUUID() + suffixName;
        // 确定文件存放的路径
        File file = new File(uploadPath + "/" + filename);
        try {
            // 上传图片到服务器（当前为本机，后续变更为服务器）
            headerImage.transferTo(file);
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器异常！", e);
        }

        // 更新当前用户的头像路径，即web访问路径
        // http://localhost:8089/community/user/header/xxx.png
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        User user = hostHolderUtil.getUser();
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{filename}", method = RequestMethod.GET)
    public void getHeaderImage(HttpServletResponse response,
                               @PathVariable("filename") String filename) {
        // 服务器存放路径
        filename = uploadPath + "/" + filename;
        // 获取文件后缀格式
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        // 先输入流从服务器读取，再用输出流从服务器读入
        try (OutputStream ops = response.getOutputStream();
             FileInputStream fis = new FileInputStream(filename);) {
            // 读取图片，同时写入
             byte[] buffer = new byte[1024];
             int b = 0;
             while ((b = fis.read(buffer)) != -1) {
                 ops.write(buffer, 0, b);
             }
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
