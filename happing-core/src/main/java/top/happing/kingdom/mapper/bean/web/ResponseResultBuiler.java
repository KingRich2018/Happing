package top.happing.kingdom.mapper.bean.web;

import top.happing.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResponseResultBuiler {

    private static final Log log = LogFactory.getLog(ResponseResultBuiler.class);

    public static <T> ResponseResult<T> success() {
        return build(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), null);
    }

    public static <T> ResponseResult<T> success(String message) {
        return build(ResponseCode.SUCCESS.getCode(), message, null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return build(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), data);
    }

    public static <T> ResponseResult<T> failure() {
        return build(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getDesc(), null);
    }

    public static <T> ResponseResult<T> failure(String message) {
        return build(ResponseCode.FAILURE.getCode(), message, null);
    }

    public static <T> ResponseResult<T> failure(T data) {
        return build(ResponseCode.FAILURE.getCode(), ResponseCode.FAILURE.getDesc(), data);
    }

    public static <T> ResponseResult<T> build(String code) {
        return build(code, null, null);
    }

    public static <T> ResponseResult<T> build(String code, String message) {
        return build(code, message, null);
    }

    public static <T> ResponseResult<T> build(String code, String message, T data) {
        ResponseResult<T> responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        responseResult.setData(data);

        if (log.isDebugEnabled()) {
            log.debug("Response result: " + JsonUtils.toJsonString(responseResult));
        }
        return responseResult;
    }

}
