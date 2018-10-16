package com.boot.service;

import com.boot.pojo.Vote;

/**
 * Created by sunshine on 2018/10/9.
 */
public interface VoteService {
    Vote getVoteById(Long voteId);

    void removeById(Long voteId);
}
