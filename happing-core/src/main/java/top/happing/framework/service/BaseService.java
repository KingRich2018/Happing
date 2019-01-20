package top.happing.framework.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

public class BaseService {
	
	@Value("${hap.proxy.enable}")
	private boolean proxy;
	@Value("${hap.proxy.port}")
	private String port="3128";
	@Value("${hap.proxy.host}")
	private String host="10.31.207.170";
	
	@PostConstruct
	public void enableProxy() {
		if(proxy) {
			System.setProperty("http.proxyHost", host);
			System.setProperty("http.proxyPort", port);
			System.setProperty("https.proxyHost", host);
			System.setProperty("https.proxyPort", port);
		}
	}
}
