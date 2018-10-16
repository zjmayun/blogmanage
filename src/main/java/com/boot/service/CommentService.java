package com.boot.service;


import com.boot.pojo.Comment;

/**
 * Created by sunshine on 2018/9/26.
 */
public interface CommentService {
     Comment getCommetById(Long commentId);

     void removeById(Long commentId);

}
