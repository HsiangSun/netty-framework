package cn.hsiangsun.dispatcher;

import cn.hsiangsun.response.HttpRespond;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求信息类
 * 
 * @author Leo
 * @date 2018/3/16
 */
@Data
public final class RequestInfo {
    
    private FullHttpRequest request;
    
    private HttpRespond response;
    
    private Map<String, Object> parameters = new HashMap<>();
    
    private Map<String, String> headers = new HashMap<>();
    
    private String body;
    
    private Map<String, String> formData = new HashMap<>();
    
    //private List<MultipartFile> files = new ArrayList<>(8);
    
}
