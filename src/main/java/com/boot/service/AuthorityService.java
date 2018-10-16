package com.boot.service;

import com.boot.pojo.Authority;
import com.boot.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by sunshine on 2018/9/26.
 */
public interface AuthorityService {

    Authority getAuthorityById(Long id);

}
