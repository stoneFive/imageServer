package com.keep.util.http;

import org.apache.http.client.config.RequestConfig;

public abstract class Request {
	
	// 最大等待时间
	private static final int WAIT_TIMEOUT = 10000;
	// 链接超时时间
	private static final int CONNECT_TIMEOUT = 50000;
	// 读取超时时间
	private static final int READ_TIMEOUT = 50000;

	protected int readTimeOut = READ_TIMEOUT;
	
	protected int connectTimeOut = CONNECT_TIMEOUT;
	
	protected int waitTimeOut = WAIT_TIMEOUT;
	
	
	protected  RequestConfig getRequestConfig(){
		return RequestConfig.custom().setSocketTimeout(readTimeOut)
		.setConnectTimeout(connectTimeOut)
		.setConnectionRequestTimeout(waitTimeOut).build();
	}
	
	/**
	 * 执行请求并返回结果
	 * @return
	 */
	public abstract String execute();
	
}
