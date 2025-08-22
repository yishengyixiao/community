package com.cys.dto;

public class GithubUser {
    private String name;        // 显示名称（可能为空）
    private String login;       // GitHub用户名（不会为空）
    private Long id;
    private String bio;
    private String avatar_url;
    private String html_url;
    private String email;
    private Integer public_repos;
    private Integer followers;
    private Integer following;

    // 原有的getter和setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // 新增的getter和setter
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(Integer public_repos) {
        this.public_repos = public_repos;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    // 获取显示名称的辅助方法
    public String getDisplayName() {
        return name != null && !name.trim().isEmpty() ? name : login;
    }

    @Override
    public String toString() {
        return "GithubUser{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", html_url='" + html_url + '\'' +
                ", email='" + email + '\'' +
                ", public_repos=" + public_repos +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}