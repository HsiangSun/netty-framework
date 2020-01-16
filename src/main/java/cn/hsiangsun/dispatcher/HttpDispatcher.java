package cn.hsiangsun.dispatcher;

import cn.hsiangsun.response.HttpRespond;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.Interceptor;

import java.util.Map;
@Slf4j
public class HttpDispatcher implements Dispatcher {
    @Override
    public void doDispatch(ChannelHandlerContext channelHandlerContext, FullHttpRequest httpRequest) {

        HttpRespond respond = new HttpRespond(channelHandlerContext);

        respond.write("{\n" +
                "  \"age\": 18,\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Leo\"\n" +
                "}");
        respond.closeChannel();

        log.debug("{} has success complete request dispatch",channelHandlerContext.name());

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
