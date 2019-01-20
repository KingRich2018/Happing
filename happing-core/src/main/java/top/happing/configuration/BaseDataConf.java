package top.happing.configuration;

import top.happing.mybatis.plugin.PaginationInterceptor;
import top.happing.mybatis.sqlsession.SqlSessionFactoryBean;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "happing.ds", name = "enable", havingValue = "true")
public class BaseDataConf {

    @Value("${happing.ds.type}")
    private String dsType;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, MybatisProperties properties, ResourceLoader resourceLoader,
                                               ObjectProvider<Interceptor[]> interceptorsProvider, ObjectProvider<DatabaseIdProvider> databaseIdProvider) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(properties.getConfigLocation())) {
            factory.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
        }
        factory.setConfiguration(properties.getConfiguration());
        if (properties.getConfigurationProperties() != null) {
            factory.setConfigurationProperties(properties.getConfigurationProperties());
        }
        Interceptor[] interceptors =  interceptorsProvider.getIfAvailable();
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType(dsType);
        if (ObjectUtils.isEmpty(interceptors)) {
            interceptors = new Interceptor[1];
            interceptors[0] = paginationInterceptor;
        } else {
            int length = interceptors.length;
            interceptors = new Interceptor[length + 1];
            interceptors[length] = paginationInterceptor;
        }
        factory.setPlugins(interceptors);

        if (databaseIdProvider.getIfAvailable() != null) {
            factory.setDatabaseIdProvider(databaseIdProvider.getIfAvailable());
        }
        if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
            factory.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
            factory.setTypeHandlersPackage(properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
            factory.setMapperLocations(properties.resolveMapperLocations());
        }

        return factory.getObject();
    }
}
