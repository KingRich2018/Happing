package top.happing.shiro.token;

import top.happing.kingdom.mapper.bean.UserInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

public class UserServiceToken extends UsernamePasswordToken {

    private UserInfo userInfo;

    public UserServiceToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
