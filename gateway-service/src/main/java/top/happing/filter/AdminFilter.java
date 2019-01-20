package top.happing.filter;


import top.happing.auth.SignChecker;
import top.happing.kingdom.mapper.bean.web.ResponseCode;
import top.happing.kingdom.mapper.bean.web.ResponseResult;
import top.happing.utils.WebUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 * 路由映射主要通过pre类型的过滤器完成，它将请求路径与配置的路由规则进行匹配，以找到需要转发的目标地址；
 * 而请求转发的部分则是由route类型的过滤器来完成，对pre类型过滤器获得的路由地址进行转发。
 */

public class AdminFilter extends ZuulFilter {

    @Value("${happing.filter.adminSecure}")
    private String adminSecure;

    @Autowired
    private SignChecker signChecker;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1001;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Boolean enablePathAdmin = (Boolean) ctx.get(FilterContants.ENABLE_PATH_ADMIN);
        return enablePathAdmin != null && Boolean.TRUE.equals(enablePathAdmin);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String sid = ctx.getRequest().getHeader("_sid");
        String auth = ctx.getRequest().getHeader("auth");
        //sid + adminSecure通过md5Hex算法是否等于auth
        ResponseResult<String> result = signChecker.checkAdmin(sid, auth, adminSecure);

        if  (ResponseCode.SUCCESS.getCode().equals(result.getCode())) {
            ctx.setSendZuulResponse(true);
        } else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            WebUtils.renderString(ctx.getResponse(), result);
        }

        return null;
    }


}
