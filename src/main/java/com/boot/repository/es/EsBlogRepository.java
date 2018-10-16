package com.boot.repository.es;

import com.boot.pojo.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by sunshine on 2018/10/15.
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog,String>{

    /**
     * distinct进行去重查询
     * @param title
     * @param summary
     * @param tags
     * @param content
     * @param pageable
     * @return
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title, String summary, String tags, String content, Pageable pageable);

    EsBlog findByBlogId(Long blogId);
}
