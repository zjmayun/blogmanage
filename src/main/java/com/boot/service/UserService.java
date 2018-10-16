package com.boot.service;

import com.boot.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * Created by sunshine on 2018/9/26.
 */
public interface UserService {

    void saveOrUpdate(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    /**
     * 根据用户名进行分页查询
     * @param name
     * @param pageable
     * @return
     */
    Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 根据用户名查询对应的用户
     */
    User findByName(String name);

    List<User> listUsersByUsernames(Collection<String> usernames);
}
