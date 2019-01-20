package top.happing.tools.utils;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/

public class ChangUtil {
    public static String expcom(String com){
        String expcom = "";
        switch (com){
            case "shentong":
                expcom = "申通快递";
                break;
            case "ems":
                expcom = "EMS快递";
                break;
            case "yuantong":
                expcom = "圆通快递";
                break;
            case "shunfeng":
                expcom = "顺风快递";
                break;
            case "zhongtong":
                expcom = "中通快递";
                break;
            case "yunda":
                expcom = "韵达快递";
                break;
            case "tiantian":
                expcom = "天天快递";
                break;
            case "huitongkuaidi":
                expcom = "汇通快递";
                break;
            case "quanfengkuaidi":
                expcom = "全峰快递";
                break;
            case "debangwuliu":
                expcom = "德邦快递";
                break;
            case "zhaijisong":
                expcom = "宅急送";
                break;
            case "jd":
                expcom = "京东快递";
                break;
            default:
                expcom = com;
                break;
        }
        return expcom;
    }
}
