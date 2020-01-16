package cn.hsiangsun.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import lombok.NonNull;

public class HttpRespond implements Response {

    private ChannelHandlerContext channelHandlerContext;

    public HttpRespond(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void write(@NonNull String responseMsg) {
        HttpResponseStatus responseStatus = HttpResponseStatus.parseLine(String.valueOf(200));
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,responseStatus, Unpooled.copiedBuffer(responseMsg, CharsetUtil.UTF_8));
        response.headers().set("Content-Length",responseMsg.length());
        //Content-Type: application/json; charset=UTF-8
        response.headers().set("Content-Type","application/json;charset=UTF-8");
        channelHandlerContext.writeAndFlush(response);
    }

    @Override
    public void closeChannel() {
        if (this.channelHandlerContext != null && this.channelHandlerContext.channel() != null){
            this.channelHandlerContext.close();
        }
    }
}
