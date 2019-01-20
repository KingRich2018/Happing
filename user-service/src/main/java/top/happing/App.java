package top.happing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



/**
 * @EnableDiscoveryClient和@EnableEurekaClient共同点就是：都是能够让注册中心能够发现，扫描到改服务。
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class App 
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
