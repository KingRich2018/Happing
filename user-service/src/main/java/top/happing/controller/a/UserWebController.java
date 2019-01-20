package top.happing.controller.a;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.happing.kingdom.mapper.bean.web.ResponseResult;
import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@RestController
@RequestMapping("/a")
public class UserWebController {
    @GetMapping("/test")
    public ResponseResult<Object> userTest(){
            return ResponseResultBuiler.success();
    }
}
