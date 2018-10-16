package com.boot.controller;

import com.boot.pojo.Authority;
import com.boot.pojo.User;
import com.boot.service.AuthorityService;
import com.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页控制器
 * 专门负责主页的一些跳转
 * Created by sunshine on 2018/9/26.
 */
@Controller
public class MainController {

    private static final Long ROLE_USER_AUTHORITY_ID=2L;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping(value = "/")
    public String root(){
        return "redirect:/index";
    }

    @GetMapping(value = "/index")
    public String index(){
        return "redirect:/blogs";
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/register")
    public String register(){
        return "/register";
    }

    @GetMapping(value = "/search")
    public String search(){
        return "search";
    }

    /**
     * 和spring security相配合
     * 验证失败时，返回对应信息
     * @param model
     * @return
     */
    @GetMapping(value = "/login-error")
    public String loginError(Model model){
         model.addAttribute("errorMsg","账号密码错误");
         model.addAttribute("loginError","true");
         return "login";
    }

    @PostMapping(value = "/register")
    public String register(User user){
        Authority authority=authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID);
        List<Authority> list=new ArrayList<>();
        list.add(authority);
        user.setAuthrities(list);
        userService.saveOrUpdate(user);
        return "redirect:/login";
    }
}
