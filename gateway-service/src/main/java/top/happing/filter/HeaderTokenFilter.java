package top.happing.filter;

import top.happing.auth.SignChecker;
import top.happing.kingdom.mapper.bean.web.ResponseCode;
import top.happing.kingdom.mapper.bean.web.ResponseResult;
import top.happing.utils.WebUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * wangbo
 */

public class HeaderTokenFilter extends ZuulFilter {

    @Autowired
    private SignChecker signChecker;


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1000;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Boolean enablePathToken = (Boolean) ctx.get(FilterContants.ENABLE_PATH_TOKEN);
        //当enablePathToken不为空或者enablePathToken为false的时候此filter不生效，enablePathToken为true则生效
        return enablePathToken == null || Boolean.TRUE.equals(enablePathToken);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String appId = request.getHeader("appId");
        String auth = request.getHeader("auth");

        ResponseResult<String> result = signChecker.check(appId, auth);

        if  (ResponseCode.SUCCESS.getCode().equals(result.getCode())) {
            ctx.setSendZuulResponse(true);
            ctx.getZuulRequestHeaders().put("token", result.getData());
        } else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            WebUtils.renderString(ctx.getResponse(), result);
        }

        return null;
    }
}
