package top.happing.tools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.happing.domain.model.ExpressInfo;
import top.happing.domain.model.Histroy;
import top.happing.domain.model.One;
import top.happing.kingdom.mapper.bean.web.ResponseResult;
import top.happing.service.ExpressService;
import top.happing.service.HistroyService;
import top.happing.service.OneService;
import top.happing.service.WeatherService;
import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@RestController
@RequestMapping("a/tools")
public class ToolsController {
    @Autowired
    ExpressService expressService;
    @Autowired
    WeatherService weatherService;
    @Autowired
    OneService oneService;
    @Autowired
    HistroyService histroyService;

    @GetMapping("/findExpress/{expressOrder}")
    public Map<String ,Object>  findExpress(@PathVariable("expressOrder") String expressOrder) throws IOException {
        List<ExpressInfo> expressInfoList = expressService.findExpress(expressOrder);
        Map<String ,Object> map = new HashMap<>();
        if(expressInfoList.size() < 0){
            map.put("message","error");
            return map;
        }
        map.put("message","ok");
        map.put("data",expressInfoList);
        return map;
    }

    @GetMapping("/findWeather/{cityCode}")
    public Map<String ,Object>  findWeather(@PathVariable("cityCode") String cityCode) throws IOException {
        return weatherService.getWeather(cityCode);
    }
    @GetMapping("/randomOne")
    public  Map<String ,Object>  randomOne() {
        List<One> oneList = oneService.randomOne();
        Map<String ,Object> map = new HashMap<>();
        if(oneList.size() < 0){
            map.put("message","error");
            return map;
        }
        map.put("message","ok");
        map.put("data",oneList);
        return map;
    }
    @GetMapping("/countbyMenu")
    public Map<String ,Object> countbyMenu(){
        int i = oneService.countbyMenu();
        Map<String ,Object> map = new HashMap<>();
        map.put("data",i);
        return map;
    }

    @GetMapping("/getToday/{date}")
    public ResponseResult<Map<String,Object>> getToday(@PathVariable("date") String date)  {
        Map<String ,Object> map = new HashMap<>();
        if("".equals(date) || null == date){
            return ResponseResultBuiler.success();
        }
        String arr[] = date.split("-");
        if(arr.length == 2){
            date = arr[0]+"/"+arr[1];
        }
        List<Histroy> list = histroyService.getToday(date);
        if(list.size() < 0){
            return ResponseResultBuiler.failure();
        }
        map.put("todayHistory",list);
        return ResponseResultBuiler.success(map);
    }
}
