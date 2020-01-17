package cn.hsiangsun.mapping;

import cn.hsiangsun.annotation.PostMapping;

import java.lang.reflect.Method;

public class PostRequestMappingRegister extends AbstractRequestMappingRegister {
    @Override
    String getMethodUrl(Method method) {
        if (method.getAnnotation(PostMapping.class) != null){
            return method.getAnnotation(PostMapping.class).value();
        }else {
            return "";
        }
    }

    @Override
    String getHttpMethod() {
        return "POST";
    }

    @Override
    void registerMapping(String url, ControllerMapping mapping) {
        ControllerMappingParameter.getPostMappings().put(url, mapping);
    }
}
