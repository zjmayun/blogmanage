package com.boot.controller;

import com.boot.pojo.Catalog;
import com.boot.pojo.User;
import com.boot.service.CatalogService;
import com.boot.utils.ConstraintViolationExceptionHandler;
import com.boot.vo.CatalogVo;
import com.boot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by sunshine on 2018/10/11.
 */
@Controller
@RequestMapping(value = "/catalogs")
public class CatagoryController {

    @Autowired
    private CatalogService catagoryService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 获取分类列表
     * @param username
     * @param model
     * @return
     */
    @GetMapping
    public String getCatalogList(@RequestParam(value="username")String username,Model model){
        User user= (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> list=catagoryService.listCatagory(user);
        //判断是否有权限
        boolean isOwner=false;
        if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
             User principal= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
             if(principal!=null&&principal.getUsername().equals(user.getUsername())){
                 isOwner=true;
             }
        }
        model.addAttribute("isCatalogsOwner",isOwner);
        model.addAttribute("catalogs",list);
        return "/userspace/u :: #catalogRepleace";
    }

    /**
     * 发表分类
     * @param catalogVo
     * @return
     */
    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVo.username)")
    public ResponseEntity<Response> createCatalog(@RequestBody  CatalogVo catalogVo){
        String username=catalogVo.getUsername();
        Catalog catalog=catalogVo.getCatalog();
        User user= (User) userDetailsService.loadUserByUsername(username);
        catalog.setUser(user);
        try{
            catagoryService.saveCatagory(catalog);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> delete(String username,@PathVariable(value = "id")Long id,Model model){
        try {
            catagoryService.removeCatagory(id);
        }catch(ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }

    @GetMapping(value = "/edit/{id}")
    public String getCatalog(@PathVariable(value = "id")Long id,Model model){
        Catalog catalog=catagoryService.getCatagoryById(id);
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

    @GetMapping(value="/edit")
    public String editCatalog(Model model){
        Catalog catalog=new Catalog(null,null);
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }

}
