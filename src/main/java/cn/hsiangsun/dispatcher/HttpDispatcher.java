package cn.hsiangsun.dispatcher;

import cn.hsiangsun.controller.HelloController;
import cn.hsiangsun.mapping.*;
import cn.hsiangsun.response.HttpRespond;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.Interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HttpDispatcher implements Dispatcher {
    @Override
    public void doDispatch(ChannelHandlerContext channelHandlerContext, FullHttpRequest httpRequest) {

        HttpRespond respond = new HttpRespond(channelHandlerContext);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setResponse(respond);
        requestInfo.setRequest(httpRequest);

        QueryStringDecoder queryStrdecoder = new QueryStringDecoder(httpRequest.uri());
        Set<Map.Entry<String, List<String>>> entrySet = queryStrdecoder.parameters().entrySet();
        entrySet.forEach(entry -> {
            requestInfo.getParameters().put(entry.getKey(), entry.getValue().get(0));
        });

        Set<String> headerNames = httpRequest.headers().names();
        for (String headerName : headerNames) {
            requestInfo.getHeaders().put(headerName, httpRequest.headers().get(headerName));
        }

        if (httpRequest.method().name().equalsIgnoreCase("GET")){
            new RequestHandler().handleRequest(requestInfo);
        }

        //log.debug("{} has success complete request dispatch",channelHandlerContext.name());

    }

    @Override
    public boolean allowExecuteInterceptor(String url, Interceptor interceptor) {
        return false;
    }

    @Override
    public ChannelFuture processOptionsRequest(FullHttpRequest request, HttpResponse response, ChannelHandlerContext channelHandlerContext) {
        return null;
    }

    @Override
    public boolean requestHeaderAllowed(String requestHeader, Map<String, String> responseHeaders) {
        return false;
    }
}
