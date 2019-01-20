package top.happing.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

public class OAuthToken implements AuthenticationToken {

    private String principal;

    private String authCode;

    private String accessToken;

    public OAuthToken(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.authCode;
    }
}
