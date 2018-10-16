package com.boot.repository;

import com.boot.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


/**
 * Created by sunshine on 2018/9/26.
 */
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * 根据用户的姓名进行分页查询
     * @param name
     * @param pageable
     * @return
     */
    Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 根据用户姓名进行查询
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     *根据用户名进行查询
     */
    User findByUsername(String username);

    List<User> findByUsernameIn(Collection<String> usernames);

}
