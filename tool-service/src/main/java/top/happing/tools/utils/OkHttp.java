package top.happing.tools.utils;

import okhttp3.*;

import java.io.IOException;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/

public class OkHttp {

    public static Response okHttpPost(String url,String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody bodys = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Request request = new Request.Builder().url(url)
                .post(bodys).addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json; charset=UTF-8").build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response okHttpGet(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .get()//可加可不加
                .url(url)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;

    }
}
