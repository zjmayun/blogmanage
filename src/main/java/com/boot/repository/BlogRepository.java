package com.boot.repository;

import com.boot.pojo.Blog;
import com.boot.pojo.Catalog;
import com.boot.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sunshine on 2018/9/29.
 */
public interface BlogRepository extends JpaRepository<Blog,Long> {

    /**
     * 根据用户名以及title 还有tags进行分页查询
     * 标签和查询公用一个接口
     * @param user
     * @param string
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLikeOrUserAndTagsLikeOrderByCreateTimeDesc(User user,String title,User user1,String tags,Pageable pageable);


    /**
     * 根据时间降序来进行排列
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user,String title,Pageable pageable);

    /**
     * 最热查询一波
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLike(User user,String title,Pageable pageable);

    /**
     * 根据分类来查询博客
     * @param catalog
     * @param pageable
     * @return
     */
    Page<Blog> findByCatalog(Catalog catalog,Pageable pageable);
}
