package com.boot.repository;

import com.boot.pojo.Catalog;
import com.boot.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sunshine on 2018/10/10.
 */
public interface CatalogRepository extends JpaRepository<Catalog,Long>{

    /**
     *根据用户搜索分类
     * @param user
     * @return
     */
     List<Catalog> findByUser(User user);

    /**
     * 根据用户和分类名称进行搜索
     * @param user
     * @param name
     * @return
     */
     List<Catalog> findByUserAndName(User user, String name);
}
