package top.happing.shiro;

import top.happing.kingdom.mapper.bean.UserInfo;

import java.io.Serializable;

public class Principal implements Serializable {
    private Long id; // 编号
    private String loginName; // 登录名
    private String name; // 姓名

    private UserInfo user;

    public Principal(Long id, String loginName, String name) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
