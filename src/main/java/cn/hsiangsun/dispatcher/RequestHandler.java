package cn.hsiangsun.dispatcher;

import cn.hsiangsun.mapping.ControllerBean;
import cn.hsiangsun.mapping.ControllerMapping;
import cn.hsiangsun.mapping.ControllerMappingParameter;
import cn.hsiangsun.mapping.ControllerMappingRegistry;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class RequestHandler {

    //find mapping
    public void handleRequest(RequestInfo requestInfo){

        ControllerMapping mappings = lookupMappings(requestInfo);
        if (mappings == null){
            System.err.println("404 Not find");
            requestInfo.getResponse().closeChannel();
        }

        // 方法参数的类型 和参数内容(无参情况下直接执行方法)
        int paramNum = mappings.getParameters().size();
        if (paramNum == 0){
            try {
                Object result = this.execute(mappings, null, null);
                requestInfo.getResponse().write(String.valueOf(result));
                requestInfo.getResponse().closeChannel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //有参情况下传递参数再执行方法(参数类型默认等于参数个数 ) todo 可以优化！
            Object[] paramValues = new Object[paramNum];
            Class<?>[] paramTypes = new Class[paramNum];

            for (int i = 0; i < paramValues.length; i++) {
                ControllerMappingParameter parameter = mappings.getParameters().get(i);

                paramValues[i] = parameter.getName();

                paramTypes[i] = parameter.getDataType();
            }

            Object result = null;
            try {
                result = this.execute(mappings, paramTypes, paramValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
            requestInfo.getResponse().write(String.valueOf(result));
            requestInfo.getResponse().closeChannel();


        }















    }

    /**
     * 查找映射
     *
     * @param requestInfo
     * @return
     */
    private ControllerMapping lookupMappings(RequestInfo requestInfo) {
        String lookupPath = requestInfo.getRequest().uri().endsWith("/")
                ? requestInfo.getRequest().uri().substring(0, requestInfo.getRequest().uri().length() - 1)
                : requestInfo.getRequest().uri();
        int paramStartIndex = lookupPath.indexOf("?");
        if(paramStartIndex > 0) {
            lookupPath = lookupPath.substring(0, paramStartIndex);
        }

        Map<String, ControllerMapping> mappings = this.getMappings(requestInfo.getRequest().method().name());
        if (mappings == null || mappings.size() == 0) {
            return null;
        }
        Set<Map.Entry<String, ControllerMapping>> entrySet = mappings.entrySet();
        for (Map.Entry<String, ControllerMapping> entry : entrySet) {
            // 完全匹配
            if (entry.getKey().equals(lookupPath)) {
                return entry.getValue();
            }
        }
        for (Map.Entry<String, ControllerMapping> entry : entrySet) {
            // 包含PathVariable
            String matcher = this.getMatcher(entry.getKey());
            if (lookupPath.startsWith(matcher)) {
                boolean matched = true;
                String[] lookupPathSplit = lookupPath.split("/");
                String[] mappingUrlSplit = entry.getKey().split("/");
                if (lookupPathSplit.length != mappingUrlSplit.length) {
                    continue;
                }
                for (int i = 0; i < lookupPathSplit.length; i++) {
                    if (!lookupPathSplit[i].equals(mappingUrlSplit[i])) {
                        if (!mappingUrlSplit[i].startsWith("{")) {
                            matched = false;
                            break;
                        }
                    }
                }
                if(matched) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 得到控制器映射哈希表
     *
     * @param httpMethod
     * @return
     */
    private Map<String, ControllerMapping> getMappings(String httpMethod) {
        if (httpMethod == null) {
            return null;
        }
        switch (httpMethod.toUpperCase()) {
            case "GET":
                return ControllerMappingRegistry.getGetMappings();
            case "POST":
                return ControllerMappingRegistry.getPostMappings();
            case "PUT":
                return ControllerMappingRegistry.getPutMappings();
            case "DELETE":
                return ControllerMappingRegistry.getDeleteMappings();
            case "PATCH":
                return ControllerMappingRegistry.getPatchMappings();
            default:
                return null;
        }
    }

    /**
     * 得到匹配url
     *
     * @param url
     * @return
     */
    private String getMatcher(String url) {
        StringBuilder matcher = new StringBuilder(128);
        for (char c : url.toCharArray()) {
            if (c == '{') {
                break;
            }
            matcher.append(c);
        }
        return matcher.toString();
    }

    /**
     * 得到Controller类的实例
     * @param
     * @return
     * @throws Exception
     */
    private Object execute(ControllerMapping mapping, Class<?>[] paramTypes, Object[] paramValues) throws Exception {
        ControllerBean bean = ControllerMappingParameter.getBean(mapping.getClassName());
        Object instance = null;
        if(bean.isSingleton()) {
            instance = ControllerMappingRegistry.getSingleton(mapping.getClassName());
        } else {
            Class<?> clazz = Class.forName(mapping.getClassName());
            instance = clazz.newInstance();
        }
        Method method = instance.getClass().getMethod(mapping.getClassMethod(), paramTypes);
        return method.invoke(instance, paramValues);
    }

}
