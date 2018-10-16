package com.boot.controller;

import com.boot.pojo.User;
import com.boot.service.BlogService;
import com.boot.service.VoteService;
import com.boot.utils.ConstraintViolationExceptionHandler;
import com.boot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;

/**
 * Created by sunshine on 2018/10/9.
 */
@Controller
@RequestMapping(value = "/votes")
public class VoteController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private VoteService voteService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> createVote(Long blogId){
        try{
            blogService.createVote(blogId);
        }catch (ConstraintViolationException e){
           return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }


    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> deleteVote(@PathVariable(value="id")Long id,Long blogId){
        boolean isOwner=false;
        User user=voteService.getVoteById(id).getUser();
        if(SecurityContextHolder.getContext().getAuthentication()!=null&&SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&!SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
             User principal= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
             if(user.getUsername().equals(principal.getUsername())){
                 isOwner=true;
             }
        }

        if(!isOwner){
            return ResponseEntity.ok().body(new Response(false,"没有操作权限"));
        }

        try{
            blogService.removeVote(id,blogId);
            voteService.removeById(id);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false,ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }
}
