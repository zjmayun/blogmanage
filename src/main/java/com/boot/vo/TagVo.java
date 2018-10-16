package com.boot.vo;

import java.io.Serializable;

/**
 * Created by sunshine on 2018/10/15.
 */
public class TagVo implements Serializable {
    private static final Long serialVersionUID=1L;

    private String name;
    private Long count;

    public TagVo(String name,Long count){
        this.name=name;
        this.count=count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
