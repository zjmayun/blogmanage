package com.boot.controller;

import com.boot.pojo.Blog;
import com.boot.pojo.Comment;
import com.boot.pojo.User;
import com.boot.repository.BlogRepository;
import com.boot.repository.CommentRepository;
import com.boot.service.BlogService;
import com.boot.service.CommentService;
import com.boot.utils.ConstraintViolationExceptionHandler;
import com.boot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;


/**
 * Created by sunshine on 2018/10/8.
 */
@Controller
@RequestMapping(value = "/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    /**
     * 获取评论列表
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping
    public String getCommentList(@RequestParam(value = "blogId")Long blogId,Model model){
        Blog blog=blogService.findById(blogId);
        List<Comment> commentList=blog.getCommentList();

        //判断是否是操作所有者
        String commentOwner="";
        if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
                 User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                 if(user!=null){
                     commentOwner=user.getUsername();
                 }
        }
        model.addAttribute("commentOwner",commentOwner);
        model.addAttribute("commentList",commentList);
        return "/userspace/blog::#mainContainerRepleace";

    }

    /**
     * 发表评论
     * @param blogId
     * @param commentContent
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createComment(@RequestParam(value = "blogId") Long blogId,@RequestParam(value = "commentContent") String commentContent){
        try {
            blogService.createComment(commentContent, blogId);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功 ",null));
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteComment(@PathVariable(value = "id")Long id,Long blogId){
         User user=commentService.getCommetById(id).getUser();
         boolean isOwner=false;
         if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                 &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
               User user1= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
               if(user1!=null&&user1.getUsername().equals(user.getUsername())){
                   isOwner=true;
               }
         }
         if(!isOwner){
               return ResponseEntity.ok().body(new Response(false,"没有权限进行访问"));
         }

         try {
             blogService.removeComment(blogId, id);
             commentService.removeById(id);
         }catch (ConstraintViolationException e){
             return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
         }catch (Exception e){
             return ResponseEntity.ok().body(new Response(false,e.getMessage()));
         }

         return  ResponseEntity.ok().body(new Response(true,"处理成功"));
    }


}
