package cn.hsiangsun.controller;

import cn.hsiangsun.annotation.GetMapping;
import cn.hsiangsun.annotation.RequestMapping;
import cn.hsiangsun.annotation.ResponseBody;
import cn.hsiangsun.annotation.RestController;
import cn.hsiangsun.response.ResponseEntity;

@RestController
@RequestMapping("/test")
public class HelloController {

    @ResponseBody
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(){
        System.err.println("我被发现啦！！！！");
        return ResponseEntity.ok("Hello from Netty");
    }

}
