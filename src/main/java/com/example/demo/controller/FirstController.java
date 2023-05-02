package com.example.demo.controller;

import com.example.demo.service.FirstService;
import com.example.demo.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/4/19 16:11
 */
@Controller
@RequestMapping("/first")
public class FirstController {

    @Autowired
    private FirstService firstService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "hello, world! This is my first demo1!";
    }


    @RequestMapping("/hello1")
    @ResponseBody
    public String getDao() {
        return firstService.find();
    }
    @RequestMapping("/http")
    public void getHttp(HttpServletRequest request, HttpServletResponse response) {
        // 获取 request 行
        // 获取请求方法
        System.out.println(request.getMethod());
        // 获取请求路径
        System.out.println(request.getServletPath());
        // 获取请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + " : " + value);
        }

//        System.out.println("response: " + response);
        // 获取请求路径中的参数值
        System.out.println(request.getParameter("code"));
        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try(PrintWriter writer = response.getWriter();) {
            writer.write("<h1>hello, http!</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 只接收get请求，/students?current = 1 & limit = 20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "996") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "1024") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {// 路径变量
        System.out.println(id);
        return "a student";
    }

    // POST请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, String age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应html数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "wuyuhan");
        modelAndView.addObject("age", 30);
        modelAndView.setViewName("/t/view");
        return modelAndView;
    }


    // 使用model而不是modelandview
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "DUT");
        model.addAttribute("age", 75);
        return "/t/view";
    }

    // json数据
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    // 如果加了@ResponseBody注解，且返回类型是一种类，Spring会自动帮你转换为JSON，
    // 在前端收到的是字符串，只有JSON格式的字符串才能在前端转换回对象
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "zhangsan");
        emp.put("age", 23);
        emp.put("salary", 25000);
        return emp;
    }

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建cookie
        Cookie cookie = new Cookie("testCookie", CommunityUtil.generateUUID());
        // 设置生效的路径
        cookie.setPath("/community/first");
        // 设置存活时间
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return "set cookie success !";
    }

    @RequestMapping(path="/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("testCookie") String testCookie) {
        System.out.println(testCookie);
        return "get Cookie success !";
    }

    @RequestMapping(path="/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession httpSession) {
        httpSession.setAttribute("id", CommunityUtil.generateUUID());
        httpSession.setAttribute("name", "Demo");
        return "set Session";
    }

    @RequestMapping(path="/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession httpSession) {
        System.out.println(httpSession.getAttribute("id"));
        System.out.println(httpSession.getAttribute("name"));
        return "get Session";
    }
}
