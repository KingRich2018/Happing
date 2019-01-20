package top.happing.configuration;



import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.happing.conf.properties.AppProperties;
import top.happing.conf.properties.IdProperties;
import top.happing.id.IdGenerator;
import top.happing.id.SnowflakeId;

@Configuration
public class AppConf{

    @Bean
    public IdGenerator snowflakeId() {

        return new SnowflakeId();
    }

}
