package cn.hsiangsun;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@ChannelHandler.Sharable
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    //处理内容
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*ByteBuf in = (ByteBuf) msg;
        log.debug("server has received {}", in);
        ctx.write(in);*/

        if (msg instanceof FullHttpRequest){
            FullHttpRequest req = (FullHttpRequest) msg;//客户端的请求对象

            if (req.method() == HttpMethod.GET){
                if(req.uri().equals("/hello")){
                    myResponse(ctx,req,"Hello Netty World!");
                }
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void myResponse(ChannelHandlerContext ctx, FullHttpRequest req ,String content){
        boolean alive = HttpUtil.isKeepAlive(req);
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content.getBytes()));
        response.headers().set("Content-Type","text/plain");
        if (!alive){
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }else {
            response.headers().set("Connection","keep-alive");
            ctx.write(response);
        }
    }

}
