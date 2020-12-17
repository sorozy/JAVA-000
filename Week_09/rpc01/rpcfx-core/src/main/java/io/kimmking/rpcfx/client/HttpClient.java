package io.kimmking.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.kimmking.rpcfx.exception.RpcfxException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    //创建HttpClient对象,多个请求复一个对象
    //创建Request对象封装请求信息(url,请求参数处理,请求头)
    //HttpClient执行request请求,返回response对象(执行http请求)
    //处理response对象(获取响应体,响应头)
    //关闭连接

    //HttpClients/HttpClientBuild创建HttpClient连接对象,后者使用连接池方式,控制连接创建和线程安全
    //HttpClientConnectionManager配置连接池
    //1. BasicHttpClientConnectionManager是它只保持一个连接,ip和port相同可以复用连接
    //2. PoolingHttpClientConnectionManager是支持多个线程同时请求,相同ip和port可以复用连接,设置最大线程数maxTotal,设置相同地址最大连接数maxPreRoute
    //3. RequestConfig设置连接超时配置(1)从连接池中获取连接ConnectionRequestTimeout(2)建立连接超时ConnectionTimeout(3)响应超时SocketTimeout
    //Request对象HttpGet,HttpPost...
    //Url配置:URIBuilder
    //请求参数:StringEntity,UrIEncoderFromEntity
    //返回值:HttpResponse HttpResponse#getEntity()
    //通过返回值获取HttpEntity HttpEntity#getContent()

    private static HttpAsyncClientBuilder asyncClientBuilder;
    private static PoolingHttpClientConnectionManager manager;
    private static HttpClientBuilder builder;
    private static RequestConfig config;
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            8, 16, 30,
            TimeUnit.MINUTES, new LinkedBlockingDeque<>(1024),
            (r, e) -> {
                logger.error("thread pool  ====" + r.toString() + "discard");
            });

    static {

        config = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(20);//最大线程数默认20
        manager.setDefaultMaxPerRoute(2);//相同地址的最大连接数默认2
        builder = HttpClientBuilder.create().setConnectionManager(manager);
        asyncClientBuilder = HttpAsyncClientBuilder.create();
    }


    //获取连接
    public static CloseableHttpClient getConnection() {
        return builder.build();
    }

    public static CloseableHttpAsyncClient getAysncConnection() {
        return asyncClientBuilder.build();
    }


    public static <T> T post(String url, Map<String, String> params, Class<T> responseClass) {
        //获取连接
        try (
                //执行请求
                CloseableHttpResponse response = getConnection().execute(
                        getRequestMethod(url, params, HttpMethod.POST,
                                new Header[]{new BasicHeader
                                        ("Content-Type", "application/json;charset=utf8")}));
        ) {
            //获取返回值
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity res = response.getEntity();
                T result = JSONObject.parseObject(EntityUtils.toString(res), responseClass);
                EntityUtils.consumeQuietly(res);
                return result;
            }
        } catch (IOException e) {
            logger.error("execute http url{} httpMethod{} fail", url, "post", e);
        }
        return null;
    }

    public static <T> T get(String url, Map<String, String> params, Class<T> responseClass) {
        //获取连接
        try (
                //执行请求
                CloseableHttpResponse response = getConnection().execute(getRequestMethod(url, params, HttpMethod.GET, null));
        ) {
            //获取返回值
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity res = response.getEntity();
                T result = JSONObject.parseObject(EntityUtils.toString(res), responseClass);
                EntityUtils.consumeQuietly(res);
                return result;
            }
        } catch (IOException e) {
            logger.error("execute http url{} httpMethod{} fail", url, "post", e);
        }
        return null;
    }

    public static HttpUriRequest getRequestMethod(String url, Map<String, String> params, HttpMethod method, Header[] headers) {
        RequestBuilder build = null;
        switch (method) {
            case POST:
                RequestBuilder.post(URI.create(url))
                        .setEntity(new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8")));
                break;
            case GET:
                build = RequestBuilder.get(URI.create(url)).addParameters(buildParam(params));
                break;
        }
        HttpUriRequest uriRequest = build.setConfig(config).build();
        if (headers != null) uriRequest.setHeaders(headers);
        return uriRequest;
    }

    private static NameValuePair[] buildParam(Map<String, String> params) {
        return (NameValuePair[]) params.entrySet().stream().map(item -> new BasicNameValuePair(item.getKey(), item.getValue())).collect(Collectors.toList()).toArray();
    }

    public static <T> T aysncPost(String url, Map<String, String> params, Class<T> responseClass) {
        CloseableHttpClient client = getConnection();
        ExecutorCompletionService<CloseableHttpResponse> service = new ExecutorCompletionService<>(pool);
        service.submit(() -> {
            return client.execute(getRequestMethod(url, params, HttpMethod.POST,
                    new Header[]{new BasicHeader
                            ("Content-Type", "application/json;charset=utf8")}));
        });
        Future<CloseableHttpResponse> take = null;
        try {
            take = service.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (CloseableHttpResponse response = take.get()) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                T result = JSONObject.parseObject(EntityUtils.toString(entity), responseClass);
                EntityUtils.consumeQuietly(entity);
                return result;
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error("execute async http url{} httpMethod{} fail", url, "post", e);
        }
        return null;
    }

    public static <T> T aysncHttpPost(String url, Map<String, String> params, Class<T> responseClass) {
        try (
                CloseableHttpAsyncClient client = getAysncConnection();
        ) {
            client.start();
            HttpUriRequest httpUriRequest = getRequestMethod(url, params, HttpMethod.POST, new Header[]{new BasicHeader
                    ("Content-Type", "application/json;charset=utf8")});
            Future<HttpResponse> future = client.execute(httpUriRequest, null);
            HttpResponse response = future.get();
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                return JSONObject.parseObject(EntityUtils.toString(entity), responseClass);
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            logger.error("execute async http url{} httpMethod{} fail", url, "post", e);
        }
        return null;
    }

}
