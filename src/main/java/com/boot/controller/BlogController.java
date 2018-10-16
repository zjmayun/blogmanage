package com.boot.controller;

import com.boot.pojo.User;
import com.boot.pojo.es.EsBlog;
import com.boot.service.EsBlogService;
import com.boot.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by sunshine on 2018/9/26.
 */
@Controller
@RequestMapping(value = "/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    @GetMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue = "new")String order,
                            @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword,
                            @RequestParam(value = "async",required = false)boolean async,
                            @RequestParam(value = "pageIndex",required = false,defaultValue = "0")Integer pageIndex,
                            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                            Model model){
        Page<EsBlog> page=null;
        List<EsBlog> list=null;

        boolean isEmpty=true;
        try{
        if(order.equals("new")){
            Sort sort=new Sort(Sort.Direction.DESC,"createTime");
            Pageable pageable=new PageRequest(pageIndex,pageSize,sort);
            page=esBlogService.listNewestEsBlog(keyword,pageable);
        }else if(order.equals("hot")){
            Sort sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
            Pageable pageable=new PageRequest(pageIndex,pageSize,sort);
            page=esBlogService.listHotestEsBlog(keyword,pageable);
        }

            isEmpty=false;
        }catch (Exception e){
            //第一次时是会报错，sort是不能进行排序的
            Pageable pageable=new PageRequest(pageIndex,pageSize);
            page=esBlogService.listEsBlog(pageable);
        }
        list=page.getContent();

        model.addAttribute("page",page);
        model.addAttribute("order",order);
        model.addAttribute("blogList",list);
        model.addAttribute("keyword",keyword);

        if(!async&&!isEmpty){
            List<EsBlog> hotest=esBlogService.listHotestTop5EsBlog();
            model.addAttribute("hotest",hotest);
            List<EsBlog> newest=esBlogService.listNewestTop5EsBlog();
            model.addAttribute("newest",newest);
            List<TagVo> tagVos=esBlogService.listTop30Tags();
            model.addAttribute("tags",tagVos);
            List<User> users=esBlogService.listTop12Users();
            model.addAttribute("users",users);
        }

        return (async==true?"/index :: #mainContainerRepleace":"/index");
    }

    @GetMapping(value = "/newest")
    public String newestBlogs(Model model){
        List<EsBlog> list=esBlogService.listNewestTop5EsBlog();
        model.addAttribute("newest",list);
        return "newest";
    }

    @GetMapping(value = "/hotest")
    public String hotestBlogs(Model model){
        List<EsBlog> list=esBlogService.listHotestTop5EsBlog();
        model.addAttribute("hotest",list);
        return "hotest";
    }

}
