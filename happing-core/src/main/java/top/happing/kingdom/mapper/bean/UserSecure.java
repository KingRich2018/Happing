package top.happing.kingdom.mapper.bean;

import top.happing.framework.domain.model.BaseEntity;

import java.io.Serializable;
import java.util.Date;

public class UserSecure extends BaseEntity<UserSecure> {

    private Long userId;
    private String token;
    private Date tokenBeginTime;
    private Date tokenEndTime;
    private String keyName;
    private String publicKey;
    private String privateKey;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenBeginTime() {
        return tokenBeginTime;
    }

    public void setTokenBeginTime(Date tokenBeginTime) {
        this.tokenBeginTime = tokenBeginTime;
    }

    public Date getTokenEndTime() {
        return tokenEndTime;
    }

    public void setTokenEndTime(Date tokenEndTime) {
        this.tokenEndTime = tokenEndTime;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
