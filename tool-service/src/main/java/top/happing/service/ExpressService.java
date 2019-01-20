package top.happing.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import top.happing.domain.model.ExpressInfo;
import top.happing.tools.utils.ChangUtil;
import top.happing.tools.utils.OkHttp;

import java.io.IOException;
import java.util.*;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@Service
public class ExpressService {
     int flag =10000000;
    public  List<ExpressInfo> findExpress(String expressOrder) throws IOException {
        String com_url = "http://www.kuaidi100.com/autonumber/autoComNum?text="+expressOrder;
        String com_json = "{}";
        Response com_response = OkHttp.okHttpPost(com_url,com_json);
        List<String> comCode = new ArrayList<>();
        if(com_response.isSuccessful()){
            String body = com_response.body().string();
            JSONObject result = JSONObject.fromObject(body);
            if (result != null && !"".equals(result.get("auto"))) {
                JSONArray jsonArray= (JSONArray) result.get("auto");
                Iterator<Object> it = jsonArray.iterator();
                while (it.hasNext()) {
                    JSONObject  jsonObject= (JSONObject) it.next();
                    if(jsonObject.getString("comCode")!=null) {
                        comCode.add(jsonObject.getString("comCode"));
                        System.out.println(jsonObject.getString("comCode"));
                    }
                }
            }
        }

        //查看物流信息
        String exp_url = "";
        String expr_data = "{}";
        Response info_response = null;
        JSONObject expresult = null;
        String com ="";
        for(int i=0;i<comCode.size();i++){
            exp_url = "http://www.kuaidi100.com/query?type="+comCode.get(i)+"&postid="+expressOrder;
            info_response = OkHttp.okHttpPost(exp_url,expr_data);
            if(info_response.isSuccessful()){
                String expbody = info_response.body().string();
                 expresult = JSONObject.fromObject(expbody);
                if (expresult != null && "200".equals(expresult.get("status"))) {
                    com = comCode.get(i);
                    break;
                }else{
                    return null;
                }
            }
        }

        List<ExpressInfo> expressInfoList = new ArrayList<>();
        JSONArray json= (JSONArray) expresult.get("data");
        Iterator<Object> it = json.iterator();
        while (it.hasNext()) {
            JSONObject  jsonObject= (JSONObject) it.next();
            ExpressInfo expressInfo = new ExpressInfo();
            expressInfo.setCom(ChangUtil.expcom(com));
            expressInfo.setTime(jsonObject.getString("time"));
            expressInfo.setContext(jsonObject.getString("context"));
            expressInfo.setNu(expressOrder);
            expressInfoList.add(expressInfo);
                }
        return expressInfoList;
    }

    public static void main(String[] args) throws IOException {

    }
}
