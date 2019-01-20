package top.happing.conf.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "happing.shiro")
public class ShiroProperties {

    private String app;

    private String loginUrl;

    private String successUrl;

    private String unauthorizedUrl;

    private String definitions;

    private long globalSessionTimeout;

    private Map<String, String> customDefinitions = new LinkedHashMap<String, String>();

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getDefinitions() {
        return definitions;
    }

    public void setDefinitions(String definitions) {
        this.definitions = definitions;
    }

    public long getGlobalSessionTimeout() {
        return globalSessionTimeout;
    }

    public void setGlobalSessionTimeout(long globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }

    public Map<String, String> getCustomDefinitions() {
        if (this.definitions != null && this.definitions.contains(";")) {
            String[] cds = this.definitions.split(";");
            for (String cd : cds) {
                String[] c = cd.split("=");
                customDefinitions.put(c[0], c[1]);
            }
        }
        return customDefinitions;
    }

    public void setCustomDefinitions(Map<String, String> customDefinitions) {
        this.customDefinitions = customDefinitions;
    }
}
