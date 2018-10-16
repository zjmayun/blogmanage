package com.boot.service;

import com.boot.pojo.User;
import com.boot.pojo.es.EsBlog;
import com.boot.vo.TagVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by sunshine on 2018/10/15.
 */
public interface EsBlogService {
    /**
     * 根据id删除博客
     * @param id
     */
    void removeEsBlog(String id);

    EsBlog getEsblogByBlogId(Long blogId);

    EsBlog updateEsBlog(EsBlog esBlog);

    /**
     * 最新博客列表展示
     * @param keyword
     * @param pageable
     * @return
     */
    Page<EsBlog> listNewestEsBlog(String keyword,Pageable pageable);

    /**
     * 最热博客列表展示
     * @param keywork
     * @param pageable
     * @return
     */
    Page<EsBlog> listHotestEsBlog(String keywork,Pageable pageable);

    /**
     * 博客列表分页
     * @param pageable
     * @return
     */
    Page<EsBlog> listEsBlog(Pageable pageable);

    /**
     * 右侧最新前五博客列表
     * @return
     */
    List<EsBlog> listNewestTop5EsBlog();

    /**
     * 右侧最热前五博客列表
     * @return
     */
    List<EsBlog> listHotestTop5EsBlog();

    /**
     *获取最热标签
     * @return
     */
    List<TagVo> listTop30Tags();

    /**
     * 获取最热用户
     * @return
     */
    List<User> listTop12Users();

}
