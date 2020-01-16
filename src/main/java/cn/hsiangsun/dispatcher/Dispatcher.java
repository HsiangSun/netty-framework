package cn.hsiangsun.dispatcher;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.omg.PortableInterceptor.Interceptor;

import java.util.Map;

/*
* Http dispatcher interface
* */
public interface Dispatcher {

    //do http request dispatch
    void doDispatch(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest);

    boolean allowExecuteInterceptor(String url, Interceptor interceptor);

    ChannelFuture processOptionsRequest(FullHttpRequest request, HttpResponse response, ChannelHandlerContext channelHandlerContext);

    boolean requestHeaderAllowed(String requestHeader, Map<String, String> responseHeaders);

}
