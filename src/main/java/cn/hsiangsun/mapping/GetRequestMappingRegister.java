package cn.hsiangsun.mapping;

import cn.hsiangsun.annotation.GetMapping;

import java.lang.reflect.Method;

public class GetRequestMappingRegister extends AbstractRequestMappingRegister {
    @Override
    String getMethodUrl(Method method) {
        if (method.getAnnotation(GetMapping.class) != null){
            return method.getAnnotation(GetMapping.class).value();
        }else {
            return "";
        }
    }

    @Override
    String getHttpMethod() {
        return "GET";
    }

    @Override
    void registerMapping(String url, ControllerMapping mapping) {
        ControllerMappingRegistry.getGetMappings().put(url, mapping);
    }
}
