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
    public String sayHello(String name){
        return "HEllo neety from controller ";
    }

    @ResponseBody
    @GetMapping("/haha")
    public String sayHaha(){
        return "HAHAHHAHAHAHAHAA AWSL";
    }

    @ResponseBody
    @GetMapping("/demo")
    public String demo(){
        return "This is a demo";
    }

}
