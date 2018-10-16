package com.boot.controller;

/**
 * Created by sunshine on 2018/9/26.
 */

import com.boot.pojo.Authority;
import com.boot.pojo.User;
import com.boot.service.AuthorityService;
import com.boot.service.UserService;
import com.boot.utils.ConstraintViolationExceptionHandler;
import com.boot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;
    /**
     * 后台模块中数据显示以及按照name like查询共用接口
     * @param pageIndex
     * @param pageSize
     * @param name
     * @param async
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView listUsers(@RequestParam(value = "async",required = false)boolean async,
                                  @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                  @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                                  @RequestParam(value = "name",required = false,defaultValue = "")String name
                                  ,Model model){
        Pageable pageable=new PageRequest(pageIndex,pageSize);
        Page<User> page=userService.findByNameLike(name,pageable);
        List<User> list=page.getContent();
        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list","userModel",model);
    }

    @GetMapping(value="/add")
    public ModelAndView createForm(Model model){
         model.addAttribute("user",new User(null,null,null,null));
         return new ModelAndView("users/add","userModel",model);
    }

    @PostMapping
    public ResponseEntity<Response> register(User user,Long authorityId){
        List<Authority> list = new ArrayList<>();
        Authority authority = authorityService.getAuthorityById(authorityId);
        list.add(authority);
        user.setAuthrities(list);
        if (user.getId() == null) {
            user.setEncodePassword(user.getPassword());
        } else {//判断密码是否做了变更
            User originalUser=userService.getUserById(user.getId());
            String orginalPassword=originalUser.getPassword();
            PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            String currentPassword=passwordEncoder.encode(user.getPassword());
            boolean isMatch=passwordEncoder.matches(orginalPassword,currentPassword);
            if(!isMatch){
                user.setEncodePassword(user.getPassword());
            }
        }
        try {
            userService.saveOrUpdate(user);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable(value = "id")Long id){
         try{
             userService.removeUser(id);
         }catch (Exception e){
             return ResponseEntity.ok().body(new Response(false,e.getMessage()));
         }
         return ResponseEntity.ok().body(new Response(true,"删除成功"));
    }

    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable(value = "id",required = false)Long id,Model model){
        User user=userService.getUserById(id);
        model.addAttribute("user",user);
        return new ModelAndView("users/edit","userModel",model);
    }

}
