package top.happing.framework.service;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import top.happing.exception.AppException;
import top.happing.utils.JsonUtils;

public class RestBaseService {

    protected static final Log log = LogFactory.getLog(RestBaseService.class);

    @Autowired
    protected RestTemplate rest;


    protected <T> T get(String serviceId, String uri,
                        Class<T> responseType, HttpServletRequest request) {
        String url = reqUrl(serviceId, uri);
        ResponseEntity<T> responseEntity = rest.exchange(url, HttpMethod.GET,
                entity(headers(request), null), responseType);
        if (responseEntity.getStatusCodeValue() != 200) {
            log.error("Failed to access " + url);
            throw new AppException("Failed to access service");
        }
        return responseEntity.getBody();
    }
    
    protected <E> E post(String serviceId, String uri,
    		Class<E> responseType, Object requestParams, HttpServletRequest request) {
    	String url = reqUrl(serviceId, uri);
    	ResponseEntity<E> responseEntity = rest.exchange(url, HttpMethod.POST,
    			entity(headers(request), requestParams), responseType);
    	if (responseEntity.getStatusCodeValue() != 200) {
    		log.error("Failed to access " + url);
    		throw new AppException("Failed to access service");
    	}
    	return responseEntity.getBody();
    }

    protected <T> T get(String serviceId, String uri,
    		ParameterizedTypeReference responseType, Object requestParams, HttpServletRequest request) {
    	String url = reqUrl(serviceId, uri);
    	ResponseEntity<T> responseEntity = rest.exchange(url, HttpMethod.GET,
    			entity(headers(request), requestParams), responseType);
    	if (responseEntity.getStatusCodeValue() != 200) {
    		log.error("Failed to access " + url);
    		throw new AppException("Failed to access service");
    	}
    	return responseEntity.getBody();
    }
    
    protected <E> E post(String serviceId, String uri,
    		ParameterizedTypeReference responseType, Object requestParams, HttpServletRequest request) {
        String url = reqUrl(serviceId, uri);
        ResponseEntity<E> responseEntity = rest.exchange(url, HttpMethod.POST,
                entity(headers(request), requestParams), responseType);
        if (responseEntity.getStatusCodeValue() != 200) {
            log.error("Failed to access " + url);
            throw new AppException("Failed to access service");
        }
        return responseEntity.getBody();
    }


    protected HttpHeaders headers(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "utf-8");  
        headers.set("Content-type", "application/json; charset=utf-8");  //header的规定  
        if(request != null) {
	        Enumeration headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            headers.add(key, value);
	        }
        }
        return headers;
    }

    protected HttpEntity<String> entity(HttpHeaders headers, Object requestParams) {
        HttpEntity<String> entity = new HttpEntity<>(
                requestParams == null ? null : JsonUtils.toJsonString(requestParams),headers);

        return entity;
    }

    protected String reqUrl(String serviceId, String uri) {
        return "http://" + serviceId + "/" + uri;
    }
}
