package com.boot.repository;

import com.boot.pojo.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by sunshine on 2018/10/9.
 */
public interface VoteRepository extends JpaRepository<Vote,Long> {
}
