package cn.hsiangsun.mapping;

import lombok.Data;

@Data
public class ControllerBean {
    private Class<?> clazz;
    private boolean singleton;
    public ControllerBean(Class<?> clazz, boolean singleton) {
        this.clazz = clazz;
        this.singleton = singleton;
    }
}
