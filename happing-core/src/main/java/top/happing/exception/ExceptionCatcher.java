package top.happing.exception;

import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;
import top.happing.web.WebHelper;
import top.happing.utils.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@ControllerAdvice
public class ExceptionCatcher {

    private static final Log LOGGER = LogFactory.getLog(ExceptionCatcher.class);

    @ExceptionHandler
    public String exception(Exception e, ServletRequest request, ServletResponse response) {
        LOGGER.error("Catch system runtime exception...", e);
        if (WebHelper.isNeedJson(request)) {
            return WebUtils.renderString((HttpServletResponse) response, e instanceof AppException ?
                    ResponseResultBuiler.failure(e.getMessage()) : ResponseResultBuiler.failure("Internal server error"));
        }

        if (e instanceof BindException || e instanceof ConstraintViolationException
                || e instanceof ValidationException) {
            return "view/error/400";
        }

        if (e instanceof UnauthorizedException || e instanceof AuthenticationException) {
            return "view/error/403";
        }

        return "view/error/500";
    }



}
