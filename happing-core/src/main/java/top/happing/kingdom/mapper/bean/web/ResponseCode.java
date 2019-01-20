package top.happing.kingdom.mapper.bean.web;

public enum ResponseCode {

    SUCCESS("000000", "success"),
    FAILURE("100000", "failure"),
    ILLEARG("100001", "illegal argument"),
    UNAUTH("200000", "not permitted");

    private final String code;
    private final String desc;

    ResponseCode(String code, String desc) {
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
