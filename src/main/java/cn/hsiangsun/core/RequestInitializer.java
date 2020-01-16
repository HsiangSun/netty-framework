package cn.hsiangsun.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandlers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/*万恶之源*/
public class RequestInitializer extends ChannelInitializer<SocketChannel> {

    //线程池数量
    private static int eventExecutorGroupThreads = Runtime.getRuntime().availableProcessors() * 2;

    //执行队列
    private static int eventExecutorGroupQueues = 1024;

    /**
     * 业务线程组
     */
    private static final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(
            eventExecutorGroupThreads, new ThreadFactory() {
        private AtomicInteger threadIndex = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "HttpRequestHandlerThread-" + this.threadIndex.incrementAndGet());
        }
    }, eventExecutorGroupQueues, RejectedExecutionHandlers.reject());

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //pipeline.addLast("decoder",new HttpRequestDecoder());
        pipeline.addLast(new HttpServerCodec());//服务解码器
        pipeline.addLast(new HttpObjectAggregator(1024));//http 消息合并处理
        //pipeline.addLast(new MyServerHandler());//业务逻辑
        pipeline.addLast(eventExecutorGroup,"httpRequestHandler",new HttpRequestHandler());
    }
}
