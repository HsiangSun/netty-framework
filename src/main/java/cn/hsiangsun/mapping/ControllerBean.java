package cn.hsiangsun.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerBean {

    private Class<?> clazz;
    private boolean singleton;

}
