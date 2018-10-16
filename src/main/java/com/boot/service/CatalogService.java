package com.boot.service;

import com.boot.pojo.Catalog;
import com.boot.pojo.User;

import java.util.List;

/**
 * Created by sunshine on 2018/10/10.
 */
public interface CatalogService {

    void saveCatagory(Catalog catagory);

    Catalog getCatagoryById(Long catagoryId);

    void removeCatagory(Long catagoryId);

    /**
     * 根据用户进行搜索分类
     * @param user
     * @return
     */
    List<Catalog> listCatagory(User user);
}
