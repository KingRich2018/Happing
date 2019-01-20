package top.happing.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;


/**
 * wangbo
 */
public class PathFilter extends ZuulFilter {

    @Value("${happing.filter.ignoreTokenUrl}")
    private String ignoreTokenUrl;

    @Value("${happing.filter.adminUrl}")
    private String adminUrl;

    private AntPathMatcher matcher = new AntPathMatcher();

    private String[] ignoreTokenUrls;
    private String[] adminUrls;

    /**
     * Type：定义filter的类别，用字符串代表，有四种标准类别，代表了Request的生命周期。filterType()返回值代表该filter的Type。
     *
     * PRE: 该类型的filters在Request routing到源web-service之前执行。用来实现Authentication、选择源服务地址等
     * ROUTING：该类型的filters用于把Request routing到源web-service，源web-service是实现业务逻辑的服务。这里使用HttpClient请求web-service。
     * POST：该类型的filters在ROUTING返回Response后执行。用来实现对Response结果进行修改，收集统计数据以及把Response传输会客户端。
     * ERROR：上面三个过程中任何一个出现错误都交由ERROR类型的filters进行处理。
     * shouldFilter：true代表生效，false代表不生效。那么什么情况下使用不生效呢，不生效干嘛还要写这个filter类呢？
     * 其实是有用的，有时我们会动态的决定让不让一个filter生效，譬如我们可能根据Request里是否携带某个参数来判断是否需要生效，或者我们需要从上一个filter里接收某个数据来决定，再或者我们希望能手工控制是否生效（使用如Appolo之类的配置中心，来动态设置该字段）
     * ---------------------
     */
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 990;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        determineFilterPath();
        return null;
    }

    @PostConstruct
    public void ininFilter() {
        ignoreTokenUrls = ignoreTokenUrl.split(";");
        adminUrls = adminUrl.split(";");
    }

    /**
     * ignoreTokenUrls:忽视token的路径
     */
    private void determineFilterPath() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestUri = ctx.getRequest().getRequestURI();
        //   ignoreTokenUrls  /**/a/**
        for (String url : ignoreTokenUrls) {
            if (matcher.match(url, requestUri)) {
                ctx.set(FilterContants.ENABLE_PATH_TOKEN, Boolean.FALSE);
            }
        }
        //   adminUrls= /**/a/** 把匹配到的路径都设置为true
        for (String url : adminUrls) {
            if (matcher.match(url, requestUri)) {
                ctx.set(FilterContants.ENABLE_PATH_ADMIN, Boolean.FALSE);
            }
        }

    }
}
