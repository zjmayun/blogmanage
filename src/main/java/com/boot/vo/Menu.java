package com.boot.vo;

import java.io.Serializable;

/**
 * 后台控制器返回给前台的封装格式
 * Created by sunshine on 2018/9/27.
 */
public class Menu implements Serializable {

    private static final Long serialVersionUID=1L;

    private String url;
    private String name;

    public Menu(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
