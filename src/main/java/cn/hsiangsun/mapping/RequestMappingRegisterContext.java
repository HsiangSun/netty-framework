package cn.hsiangsun.mapping;

import java.lang.reflect.Method;

public final class RequestMappingRegisterContext {
    private RequestMappingRegister strategy;

    public RequestMappingRegisterContext(RequestMappingRegister strategy) {
        this.strategy = strategy;
    }

    /**
     * 注册 Mapping
     * @param clazz
     * @param baseUrl
     * @param method
     */
    public void registerMapping(Class<?> clazz, String baseUrl, Method method) {
        if(this.strategy == null) {
            return;
        }
        this.strategy.register(clazz, baseUrl, method);
    }
}
