package com.boot.pojo;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Created by sunshine on 2018/9/27.
 */
@Entity
public class Authority implements GrantedAuthority{

    private static final Long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Authority(Long id,String name) {
        this.name = name;
        this.id=id;
    }

    protected Authority(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
