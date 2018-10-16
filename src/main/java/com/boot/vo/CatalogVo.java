package com.boot.vo;

import com.boot.pojo.Catalog;

import java.io.Serializable;

/**
 * Created by sunshine on 2018/10/11.
 */
public class CatalogVo implements Serializable {

    private static final Long serialVersionUID=1L;

    private Catalog catalog;
    private String username;

    public CatalogVo(){}

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
