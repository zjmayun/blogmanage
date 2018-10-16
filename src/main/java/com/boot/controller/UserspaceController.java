package com.boot.controller;

import com.boot.pojo.Blog;
import com.boot.pojo.Catalog;
import com.boot.pojo.User;
import com.boot.pojo.Vote;
import com.boot.service.BlogService;
import com.boot.service.CatalogService;
import com.boot.service.UserService;
import com.boot.utils.ConstraintViolationExceptionHandler;
import com.boot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 用户主页控制器，进行用户个人的设置
 * Created by sunshine on 2018/9/26.
 */
@Controller
@RequestMapping(value = "/u")
public class UserspaceController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    @GetMapping(value = "/{username}")
    public String blogs(@PathVariable(value = "username")String username,Model model){
        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return "redirect:/u/"+username+"/blogs";
    }

    /**
     * 进入到博主个人设置页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping(value = "/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable(value = "username")String username, Model model){
        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/profile","userModel",model);
    }


    /**
     * 博主保持个人设置的信息
     * @param username
     * @param user
     * @return
     */
    @PostMapping(value = "/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveprofile(@PathVariable(value = "username")String username,User user){
        User originalUser= userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());
        //验证密码是否发生了变更
        String originalPassword=originalUser.getPassword();
        PasswordEncoder encoder=new BCryptPasswordEncoder();
        String currentPassword=encoder.encode(user.getPassword());
        boolean isMatch=encoder.matches(originalPassword,currentPassword);
        if(!isMatch){
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveOrUpdate(originalUser);
        return "redirect:/u/"+username+"/profile";
    }

    /**
     * 获取编辑用户图像界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping(value = "/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable(value = "username")String username, Model model){
        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    /**
     * 保存用户图像
     * @param username
     * @param user
     * @return
     */
    @PostMapping(value = "/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable(value = "username")String username, @RequestBody User user){
        String avatarUrl=user.getAvatar();
        User originalUser=userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpdate(originalUser);
        return ResponseEntity.ok().body(new Response(true,"处理成功",avatarUrl));
    }

    /**
     * 这个代码有点多，其实思维也就这么多
     * 用户博客列表的主页进行显示
     * @param username
     * @param order
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping(value="/{username}/blogs")
    public String listBlogsByOrder(@PathVariable(value = "username")String username,
            @RequestParam(value = "order",required=false,defaultValue="new")String order,
            @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword,
            @RequestParam(value = "catalog",required = false)Long catalogId,
            @RequestParam(value = "async",required = false)boolean async,
            @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,Model model){

        User user= (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);

        Page<Blog> page=null;
        //根据排序方式的不同
        if(catalogId!=null&&catalogId>0){
            Catalog catalog=catalogService.getCatagoryById(catalogId);
            Pageable pageable=new PageRequest(pageIndex,pageSize);
            page=blogService.listBlogsByCatalog(catalog,pageable);
            order="";
        }else if(order.equals("hot")){
            Sort sort=new Sort(Sort.Direction.DESC,"comments","votes","reading");
            Pageable pageable=new PageRequest(pageIndex,pageSize,sort);
            page=blogService.listBlogsByTitleAndSorts(user,keyword,pageable);
        }else  if(order.equals("new")){
            Sort sort=new Sort(Sort.Direction.DESC,"createTime");
            Pageable pageable=new PageRequest(pageIndex,pageSize,sort);
            page=blogService.listBlogsByTitle(user,keyword,pageable);
        }
        List<Blog> list=page.getContent();
        model.addAttribute("blogList",list);
        model.addAttribute("page",page);
        model.addAttribute("order",order);
        model.addAttribute("catalogId",catalogId);
        model.addAttribute("user",user);
        model.addAttribute("keyword",keyword);

        return (async==true?"/userspace/u::#mainContainerRepleace":"/userspace/u");
    }


     /**
     * 获取博客展示界面
     */
     @GetMapping(value="/{username}/blogs/{id}")
     public String getById(@PathVariable(value="username")String username,@PathVariable(value = "id")Long id,Model model){
         //进入博客界面，就代表阅读了一次
         blogService.readIncreasing(id);
         User principal=null;
         boolean isOwner=false;

         //判断是否是博客的作者
         if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                 &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
               principal= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
               if(principal!=null&&username.equals(principal.getUsername())){
                   isOwner=true;
               }
         }
         Blog blog=blogService.findById(id);
         List<Vote> votes=blog.getVoteList();
         Vote currentVote=null;
//         for(int i=0;i<votes.size();i++){
//             if(votes.get(i).getUser().getUsername().equals(principal.getUsername())){
//                 currentVote=votes.get(i);
//                 break;
//             }
//         }
         for(Vote vote:votes){
             if(principal!=null) {
                 if (vote.getUser().getUsername().equals(principal.getUsername())) {
                     currentVote = vote;
                     break;
                 }
             }else {
                 break;
             }
         }
         model.addAttribute("isBlogOwner",isOwner);
         model.addAttribute("blogModel",blogService.findById(id));
         model.addAttribute("currentVote",currentVote);
         return "/userspace/blog";
     }

    /**
     * 删除博客
     * @param username
     * @param id
     * @return
     */
     @DeleteMapping(value = "/{username}/blogs/{id}")
     @PreAuthorize("authentication.name.equals(#username)")
     public ResponseEntity<Response> delete(@PathVariable(value = "username")String username,@PathVariable(value = "id")Long id){
         try {
             blogService.remove(id);
         }catch (Exception e){
             return ResponseEntity.ok().body(new Response(false,"删除失败",e.getMessage()));
         }
         String redirectUrl="/u/"+username+"/blogs";
         return ResponseEntity.ok().body(new Response(true,"删除成功",redirectUrl));
     }

    /**
     *获取博客新增界面
     * @param model
     * @return
     */
     @GetMapping(value="/{username}/blogs/edit")
     public ModelAndView blogEdit(@PathVariable(value="username")String username,Model model){
         User user= (User) userDetailsService.loadUserByUsername(username);
         List<Catalog> list=catalogService.listCatagory(user);
         model.addAttribute("catalogs",list);
         model.addAttribute("blog",new Blog(null,null,null));
         return  new ModelAndView("/userspace/blogedit","blogModel",model);
     }

    /**
     *获取博客编辑界面
     * @param model
     * @return
     */
    @GetMapping(value="/{username}/blogs/edit/{id}")
    public ModelAndView blogEditById(@PathVariable(value="username")String username,@PathVariable(value = "id")Long id,Model model){
        User user= (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> list=catalogService.listCatagory(user);
        model.addAttribute("catalogs",list);
        model.addAttribute("blog",blogService.findById(id));
        return  new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 保存博客的接口
     * @param username
     * @param blog
     * @return
     */
    @PostMapping(value ="{username}/blogs/edit" )
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable(value = "username")String username,@RequestBody Blog blog){
        if(blog.getCatalog().getId()==null){
            return ResponseEntity.ok().body(new Response(false,"未进行分类"));
        }

        try {
            if (blog.getId() != null) {
                Blog originalBlog = blogService.findById(blog.getId());
                originalBlog.setTitle(blog.getTitle());
                originalBlog.setSummary(blog.getSummary());
                originalBlog.setContent(blog.getContent());
                originalBlog.setTags(blog.getTags());
                blogService.saveOrUpdate(originalBlog);
            } else {
                User user = (User) userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveOrUpdate(blog);
            }
        }
        catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl="/u/"+username+"/blogs/"+blog.getId();
        return ResponseEntity.ok().body(new Response(true,"操作成功",redirectUrl));
    }





}

