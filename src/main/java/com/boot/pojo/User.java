package com.boot.pojo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sunshine on 2018/9/26.
 */
@Entity
public class User implements UserDetails,Serializable{

    private static final Long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增长策略
    private Long id;

    @NotEmpty(message = "name不能为空")
    @Size(min = 2,max = 20)
    @Column(nullable = false,length = 20)
    private String name;

    @NotEmpty(message = "username不能为空")
    @Size(min=2,max=20)
    @Column(nullable = false,unique = true,length = 20)//username为unique
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 6,max = 100)
    private String password;

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "email格式错误")
    @Column(nullable = false,length = 20,unique = true)
    private String email;

    @Column(length = 50)
    private String avatar;//用户图像的地址

    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id",referencedColumnName = "id"))
    private List<Authority> authrities;

    protected User(){}//jpa默认规范

    public User(String name, String username, String email, String avatar) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthrities(List<Authority> authrities) {
        this.authrities = authrities;
    }

    //要来进行转换
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleAuthorities=new ArrayList<>();
        for(GrantedAuthority authority:this.authrities){
            simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return simpleAuthorities;
    }

    public String getPassword() {
        return password;
    }

    //注册用户的时候需要进行加密
    public void setEncodePassword(String password){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String transPassword=passwordEncoder.encode(password);
        this.password=transPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
