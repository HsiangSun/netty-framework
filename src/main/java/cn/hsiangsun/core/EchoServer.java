package cn.hsiangsun.core;

import cn.hsiangsun.mapping.ControllerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Properties;

@Slf4j
public class EchoServer {
    private static Integer port = 2333;

//    public EchoServer(int port) {
//        this.port = port;
//    }

    static {
        try {
            InputStream in = new FileInputStream("src/main/resources/application.properties");
            Properties properties = new Properties();
            properties.load(in);
            String o = (String) properties.get("server.port");
            System.err.println(o);
            int portInt = Integer.parseInt(o);
            EchoServer.port = portInt;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void start() throws Exception{

        new ControllerFactory().registerController("cn.hsiangsun.controller");
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    /*.childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            //socketChannel.pipeline().addLast(new MyServerHandler());
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());//服务解码器
                            pipeline.addLast(new HttpObjectAggregator(1024));//http 消息合并处理
                            pipeline.addLast(new RequestInitializer());//业务逻辑
                        }
                    });*/
                    .childHandler(new RequestInitializer());
            Channel channel = bootstrap.bind().sync().channel();
            log.info("【server has set up @127.0.0.1:{} 】",port);
            channel.closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws Exception {
        new EchoServer().start();
    }

}
