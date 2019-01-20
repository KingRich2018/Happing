package top.happing.auth;

public enum AuthResponseCode {
    AUTH_INVALID("200001", "请求无效"),
    AUTH_NO_SECURE("200002", "非法用户"),
    TOKEN_INVALID("200003", "账号已退出"),
    TOKEN_EXPIRE("200004", "请重新登录"),
    REQ_TIMEOUT("200005", "请求超时"),
    REQ_REPLAY("200006", "请求已接收");

    private final String code;
    private final String desc;

    AuthResponseCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

