package cn.hsiangsun.dispatcher;

import cn.hsiangsun.controller.HelloController;
import cn.hsiangsun.mapping.*;
import cn.hsiangsun.response.HttpRespond;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
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

        String uri = httpRequest.uri();

        System.out.println(uri);

        Map<String, ControllerMapping> getMappings = ControllerMappingRegistry.getGetMappings();
        // TODO: 2020/1/16 delete
        System.err.println("mapping size = "+getMappings.size());



        Object invoke = null;
        Set<Map.Entry<String, ControllerMapping>> entries = getMappings.entrySet();
        for (Map.Entry<String, ControllerMapping> entry : entries) {

            ControllerMapping mapping = entry.getValue();

            System.err.println(entry.getKey()+"==>"+entry.getValue());

            if(uri.equalsIgnoreCase(mapping.getUrl())){
                System.err.println("成功匹配！！");
                try {
                    Class<?> aClass = Class.forName(entry.getKey());
                    Object o = aClass.newInstance();
                    List<ControllerMappingParameter> parameters = mapping.getParameters();
                    Class[] classes = null;
                    for (int i = 0; i < parameters.size(); i++) {
                        classes[i] = parameters.get(i).getDataType();
                    }

                    Method method = aClass.getMethod(mapping.getClassMethod(), classes);

                    invoke = method.invoke(o);

                    System.out.println("成功执行方法！！！"+invoke);


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }




        HttpRespond respond = new HttpRespond(channelHandlerContext);

        respond.write(invoke.toString());
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
