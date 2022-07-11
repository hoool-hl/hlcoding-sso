package com.hlcoding.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {
    @Value("${sso.server.url}")
    String ssoServerUrl;

    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        Class<HelloController> clazz = HelloController.class;

        return "hello sso";
    }

    @GetMapping("/employees")
    public String employees(Model model, HttpSession session,@RequestParam(value="token",required = false) String token){
        if(!StringUtils.isEmpty(token)){
            RestTemplate restTemplate=new RestTemplate();
            ResponseEntity<String> forEntity = restTemplate.getForEntity("http://ssoserver.com:9090/userinfo?token=" + token, String.class);
            String body = forEntity.getBody();
            session.setAttribute("loginUser", body);
        }
        Object loginUser = session.getAttribute("loginUser");
        if(loginUser == null){
            return "redirect:"+ssoServerUrl + "?redirect_url=http://clint1:9091/employees";
        }
        List<String> list = new ArrayList<>();
        list.add("zhangshang");
        list.add("李四");
        model.addAttribute("emps",list);
        return "list";
    }

}
