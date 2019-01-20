package top.happing.conf;

import top.happing.auth.SignChecker;
import top.happing.auth.SignRepo;
import top.happing.filter.AdminFilter;
import top.happing.filter.HeaderTokenFilter;
import top.happing.filter.PathFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * wangbo
 */
@Configuration
public class AuthConf {

    @Bean
    public SignChecker signChecker() {
        return new SignChecker();
    }

    @Bean
    public SignRepo signRepo() {
        return new SignRepo();
    }

    @Bean
    public HeaderTokenFilter headerTokenFilter() {
        return new HeaderTokenFilter();
    }

    @Bean
    public PathFilter pathFilter() {
        return new PathFilter();
    }

    @Bean
    public AdminFilter adminFilter() {
        return new AdminFilter();
    }

}
