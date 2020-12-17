package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.exception.RpcfxException;
import io.kimmking.rpcfx.server.RpcfxInvoker;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public final class CglibRpcfx {

    private static final Logger logger = LoggerFactory.getLogger(CglibRpcfx.class);

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T> T create(final Class<T> serviceClass, final String url) {
        // 0. 替换动态代理 -> AOP
        // 使用cglib代理方式
        return (T) Enhancer.create(serviceClass, null, new RpcfxInterceptor(serviceClass, url));
    }

    public static class RpcfxInterceptor implements MethodInterceptor {

        public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;

        public <T> RpcfxInterceptor(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        private RpcfxResponse post(RpcfxRequest req, String url) {
            //使用HttpClient
            Map<String,String> params = JSONObject.parseObject(JSON.toJSONString(req), Map.class);
            return HttpClient.aysncHttpPost(url,params,RpcfxResponse.class);
            // 1.可以复用client
            // 2.尝试使用httpclient或者netty client
//            OkHttpClient client = new OkHttpClient();
//            final Request request = new Request.Builder()
//                    .url(url)
//                    .post(RequestBody.create(JSONTYPE, reqJson))
//                    .build();
//            String respJson = client.newCall(request).execute().body().string();
//            System.out.println("resp json: " + respJson);
//            return JSON.parseObject(respJson, RpcfxResponse.class);
        }

        @Override
        public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);
            RpcfxResponse response = post(request, url);
            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException
            if (response != null && !response.isStatus()) {
                RpcfxException e = response.getRpcfxException();
                logger.error(request.getServiceClass() + "#" + request.getMethod() + "invoke an error occurs" + e.getMessage(), e);
                throw e;
            }
            return JSON.parse(response.getResult().toString());
        }
    }
}
