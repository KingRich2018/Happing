package top.happing.configuration;

import top.happing.conf.properties.ServiceProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestClientConf {

    @Bean
    public ServiceProperties serviceIdProps() {
        return new ServiceProperties();
    }

    @Bean
    @Qualifier("restTemplate")
    @Primary
    @LoadBalanced
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplateBuilder().requestFactory(
                requestFactory).build();
        return restTemplate;
    }


    @Bean
    @Qualifier("nonLBRestTemplate")
    public RestTemplate nonLBRestTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplateBuilder().requestFactory(
                requestFactory).build();
        return restTemplate;
    }
}
