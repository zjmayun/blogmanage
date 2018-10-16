package com.boot.service.impl;

import com.boot.pojo.Catalog;
import com.boot.pojo.User;
import com.boot.repository.CatalogRepository;
import com.boot.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by sunshine on 2018/10/10.
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catagoryRepository;

    /**
     * 保存分类时需要做判断
     * @param catagory
     */
    @Transactional
    @Override
    public void saveCatagory(Catalog catagory) {
        List<Catalog> list=catagoryRepository.findByUserAndName(catagory.getUser(),catagory.getName());
        if(list!=null&&list.size()>0){
            throw new IllegalArgumentException("分类已存在");
        }
         catagoryRepository.save(catagory);
    }

    @Override
    public Catalog getCatagoryById(Long catagoryId) {
        return catagoryRepository.findOne(catagoryId);
    }

    @Transactional
    @Override
    public void removeCatagory(Long catagoryId) {
        catagoryRepository.delete(catagoryId);
    }

    @Override
    public List<Catalog> listCatagory(User user) {
        return catagoryRepository.findByUser(user);
    }
}
