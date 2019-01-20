package top.happing.service;

import net.sf.json.JSONObject;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import top.happing.tools.utils.HttpReq;
import top.happing.tools.utils.OkHttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@Service
public class WeatherService {
    public static Map<String ,Object>  getWeather(String cityCode) throws IOException {
        Map<String ,Object> map = new HashMap<>();
        if(cityCode == null || "".equals(cityCode)){
            return map;
        }
        String url = "http://t.weather.sojson.com/api/weather/city/"+cityCode;
        Response response = OkHttp.okHttpGet(url);
        if(response.isSuccessful()) {
            String body = response.body().string();
            JSONObject result = JSONObject.fromObject(body);
            if(result != null && !"Success".equals(result.get("message"))){
                map.put("message","ok");
                map.put("weatherData",result.get("data"));
            }
        }
        if(map.get("message")!="ok"){
            return map;
        }
        //实时天气
        String realTimeurl = "http://www.weather.com.cn/data/sk/"+cityCode+".html";
        String body = HttpReq.getJsonByInternet(realTimeurl);
        if(body != null && !"".equals(body)){
            map.put("realTime",JSONObject.fromObject(body));
            return map;
        }
        map.put("message","error");
        return map;
    }
    public static void main(String[] args) throws IOException {
        System.out.println(WeatherService.getWeather("101030100").get("data"));
    }
}
