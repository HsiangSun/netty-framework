package cn.hsiangsun.mapping;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求映射类
 *
 * @author Leo
 * @date 2018/3/16
 */
@Data
public final class ControllerMapping {

    private String url;

    private String className;

    private String classMethod;

    private List<ControllerMappingParameter> parameters = new ArrayList<ControllerMappingParameter>();

    /**
     * 是否输出结果为JSON
     */
    private boolean JsonResponse;

    /**
     * 单例类
     */
    private boolean singleton;

}
