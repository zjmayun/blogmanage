package com.boot.repository;

import com.boot.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sunshine on 2018/10/8.
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
