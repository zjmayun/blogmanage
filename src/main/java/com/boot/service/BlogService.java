package com.boot.service;

import com.boot.pojo.Blog;
import com.boot.pojo.Catalog;
import com.boot.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by sunshine on 2018/9/26.
 */
public interface BlogService {

     Blog findById(Long id);

     void remove(Long id);

     void saveOrUpdate(Blog blog);

     Page<Blog> listBlogsByTitleAndSorts(User user,String title,Pageable pageable);

     Page<Blog> listBlogsByTitle(User user,String title,Pageable pageable);

     void readIncreasing(Long blogId);

     Blog createComment(String comment,Long blogId);

     void removeComment(Long blogId,Long commentId);

     Blog createVote(Long blogId);

     void removeVote(Long voteId,Long blogId);

     Page<Blog> listBlogsByCatalog(Catalog catalog,Pageable pageable);


}
