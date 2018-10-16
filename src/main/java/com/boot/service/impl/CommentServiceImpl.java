package com.boot.service.impl;

import com.boot.pojo.Comment;
import com.boot.repository.CommentRepository;
import com.boot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sunshine on 2018/10/8.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommetById(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    @Override
    public void removeById(Long commentId) {
        commentRepository.delete(commentId);
    }
}
