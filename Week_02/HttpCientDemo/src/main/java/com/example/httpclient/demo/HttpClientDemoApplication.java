package com.example.httpclient.demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class HttpClientDemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientDemoApplication.class);

    public static void main(String[] args) {
        String url = "http://127.0.0.1:8801";

        String response = getRequestByApacheHttp(url);
        logger.info("Apache HttpCliet Response = {}", response);

        String response2 = getRequestByOkHttp(url);
        logger.info("OkHttp Response = {}", response2);
    }

    private static String getRequestByApacheHttp(String url) {
        String responseBody = "Request failed";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            responseBody = httpClient.execute(httpGet, httpResponse -> {
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status < 200 || status >= 300) {
                    return null;
                }
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            });
            // ... do something with response
            if (StringUtils.isEmpty(responseBody)) {
                return responseBody;
            }
        } catch (IOException e) {
            // ... handle IO exception
        }
        return responseBody;
    }

    private static String getRequestByOkHttp(String url) {
        String responseBody = "Request failed";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // ... handle failed request
                return responseBody;
            }
            responseBody = response.body().string();
            // ... do something with response
            if (StringUtils.isEmpty(responseBody)) {
                return responseBody;
            }
        } catch (IOException e) {
            // ... handle IO exception
        }
        return responseBody;
    }
}