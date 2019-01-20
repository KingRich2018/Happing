package top.happing.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebUtils {

    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    public static String renderString(HttpServletResponse response, Object object) {
        return renderString(response, JsonUtils.toJsonString(object), "application/json");
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param string
     * @return
     */
    public static String renderString(HttpServletResponse response, String string, String type) {
        try {
            response.reset();
            response.setContentType(type);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static String buildRequestParams(String uri, String[] params) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        for (int i = 0; i < params.length; i = i + 2) {
            parameters.add(new BasicNameValuePair(params[i], params[i + 1]));
        }
        return uri + "?" + URLEncodedUtils.format(parameters, "UTF-8");
    }

    public static boolean containsInHeader(HttpServletRequest request, String name) {
        return StringUtils.isNotBlank(request.getHeader(name));
    }

    public static String executeHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(request);
            Integer code = response.getStatusLine().getStatusCode();
            HttpEntity loginEntity = response.getEntity();
            String entityContent = EntityUtils.toString(loginEntity, "UTF-8");
            if (code == 200) {
                return entityContent;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
