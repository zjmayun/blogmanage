package com.boot.service.impl;

import com.boot.pojo.*;
import com.boot.pojo.es.EsBlog;
import com.boot.repository.BlogRepository;
import com.boot.service.BlogService;
import com.boot.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by sunshine on 2018/9/29.
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Override
    public Blog findById(Long id) {
        return blogRepository.findOne(id);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        EsBlog esBlog=esBlogService.getEsblogByBlogId(id);
        blogRepository.delete(id);
        esBlogService.removeEsBlog(esBlog.getId());
    }

    @Transactional
    @Override
    public void saveOrUpdate(Blog blog) {
        boolean isNew=(blog.getId()==null);
        Blog returnBlog=blogRepository.save(blog);
        EsBlog esBlog=null;
        if(isNew){
           esBlog=new EsBlog(blog);
        }else{
           esBlog=esBlogService.getEsblogByBlogId(returnBlog.getId());
           esBlog.update(returnBlog);
        }
        esBlogService.updateEsBlog(esBlog);
    }

    /**
     * 根据时间降序进行排序
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlogsByTitleAndSorts(User user, String title, Pageable pageable) {
        title="%"+title+"%";
        return blogRepository.findByUserAndTitleLike(user,title,pageable);
    }

    @Override
    public Page<Blog> listBlogsByTitle(User user, String title, Pageable pageable) {
        title="%"+title+"%";
        String tags=title;
        return blogRepository.findByUserAndTitleLikeOrUserAndTagsLikeOrderByCreateTimeDesc(user,title,user,tags,pageable);
    }

    /**
     * 每次进入博客列表，代表阅读量加1
     * @param blogId
     */
    @Override
    public void readIncreasing(Long blogId) {
        Blog blog=blogRepository.findOne(blogId);
        blog.setReading(blog.getReading()+1);
        EsBlog esBlog=esBlogService.getEsblogByBlogId(blog.getId());
        esBlog.update(blog);
        blogRepository.save(blog);
        esBlogService.updateEsBlog(esBlog);
    }

    /**
     * 给博客添加评论
     * @param comment
     * @param blogId
     * @return
     */
    public Blog createComment(String comment,Long blogId){
        Blog originalBlog=blogRepository.findOne(blogId);
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment1=new Comment(user,comment);
        originalBlog.addComment(comment1);
        return blogRepository.save(originalBlog);
    }

    public void removeComment(Long blogId,Long commentId){
        Blog originalBlog=blogRepository.findOne(blogId);
        originalBlog.removeComment(commentId);
        blogRepository.save(originalBlog);
    }

    /**
     * 增加点赞
     * @param blogId
     * @return
     */
    public Blog createVote(Long blogId){
        Blog originalBlog=blogRepository.findOne(blogId);
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote=new Vote(user);
        boolean isExists=originalBlog.addVotes(vote);
        if(isExists){
            throw  new IllegalArgumentException("该用户已经进行过点赞");
        }
        return blogRepository.save(originalBlog);
    }

    public void removeVote(Long voteId,Long blogId){
        Blog originalBlog=blogRepository.findOne(blogId);
        originalBlog.removeVote(voteId);
        blogRepository.save(originalBlog);
    }

    /**
     * 根据分类来查询博客列表
     * @param catalog
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        return blogRepository.findByCatalog(catalog,pageable);
    }

}
