package com.boot.service.impl;

import com.boot.pojo.Vote;
import com.boot.repository.VoteRepository;
import com.boot.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by sunshine on 2018/10/9.
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long voteId) {
        return voteRepository.findOne(voteId);
    }

    @Override
    @Transactional
    public void removeById(Long voteId) {
        voteRepository.delete(voteId);
    }
}
