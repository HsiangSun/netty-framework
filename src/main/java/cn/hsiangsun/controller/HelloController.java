package cn.hsiangsun.controller;

import cn.hsiangsun.annotation.GetMapping;
import cn.hsiangsun.annotation.RequestMapping;
import cn.hsiangsun.annotation.ResponseBody;
import cn.hsiangsun.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HelloController {

    @ResponseBody
    @GetMapping("/hello")
    public String sayHello(){
        System.err.println("我被发现啦！！！！");
        return "HEllo neety from controller ";
    }

    @ResponseBody
    @GetMapping("/haha")
    public String sayHaha(){
        //System.err.println("我被发现啦！！！！");
        return "HAHAHHAHAHAHAHAA AWSL";
    }

}
