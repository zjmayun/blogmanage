package com.boot.controller;

import com.boot.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台处理器
 * 返回后台界面左侧的数据
 * Created by sunshine on 2018/9/26.
 */
@Controller
@RequestMapping(value = "/admins")
public class AdminController {

    @GetMapping
    public ModelAndView listMenu(Model model){
        List<Menu> list=new ArrayList<>();
        list.add(new Menu("/users","用户管理"));
        list.add(new Menu("/blogs","博客管理"));
        model.addAttribute("list",list);
        return new ModelAndView("admins/index","model",model);
    }
}
