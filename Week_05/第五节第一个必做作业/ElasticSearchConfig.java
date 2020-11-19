package com.zhao.rocketmq.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Component
public class ElasticSearchConfig {

    private String host;
    private Integer port;

    //初始化RestHighLevelClient
    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        //RestClient客户端构建器对象
        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(host, port, "http"));
        //操作es的高级rest客户端对象
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return restHighLevelClient;
    }
    //getter ,setter,toString..省略
}