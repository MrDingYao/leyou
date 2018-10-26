package com.leyou.auth.entity;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 22 18:46
 **/
public class UserInfo {

    private Long id;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserInfo(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserInfo() {
    }
}
