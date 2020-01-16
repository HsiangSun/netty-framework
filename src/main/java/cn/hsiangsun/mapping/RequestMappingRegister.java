package cn.hsiangsun.mapping;

import java.lang.reflect.Method;

public interface RequestMappingRegister {
    void register(Class<?> clazz, String baseUrl, Method method);
}
