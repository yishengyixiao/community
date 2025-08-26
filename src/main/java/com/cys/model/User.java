package com.cys.model;

public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;

    // --- 新增下面这些字段，与 GithubUser 和 index.html 对应 ---
    private String bio;
    private Integer public_repos;
    private Integer followers;
    private Integer following;
    private String html_url;

    // --- 新增的 Getters and Setters ---
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Integer getPublic_repos() { return public_repos; }
    public void setPublic_repos(Integer public_repos) { this.public_repos = public_repos; }

    public Integer getFollowers() { return followers; }
    public void setFollowers(Integer followers) { this.followers = followers; }

    public Integer getFollowing() { return following; }
    public void setFollowing(Integer following) { this.following = following; }

    public String getHtml_url() { return html_url; }
    public void setHtml_url(String html_url) { this.html_url = html_url; }

    public String getDisplayName() {
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }
        return accountId;
    }

    // 标准的getter和setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(Long gmtCreate) { this.gmtCreate = gmtCreate; }

    public Long getGmtModified() { return gmtModified; }
    public void setGmtModified(Long gmtModified) { this.gmtModified = gmtModified; }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountId='" + accountId + '\'' +
                ", token='" + token + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}