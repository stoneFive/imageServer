package com.keep.util.http;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理GET请求
 */
public final class GetRequest extends Request{

	private static final Logger logger = LoggerFactory.getLogger(GetRequest.class);
	
	private HttpGet get = null;
	
	private HttpClient client = null;
	
	private static final String DEFAULTCHARSET = "UTF-8";
	
	private String charset = DEFAULTCHARSET;
	
	public GetRequest(HttpClient client,String url){
		this.client = client;
		get = new HttpGet(url);
		get.setConfig(getRequestConfig());
	}
	
	public GetRequest(HttpClient httpClient,String url,
			String charset) {
		this(httpClient,url);
		this.charset = charset;
	}

	public GetRequest addHeader(String name,Object value){
		if (value==null) {
			return this;
		}
		get.addHeader(name, String.valueOf(value));
		return this;
	}
	
	public String execute(){
		String result = null;
		try {
	        long startTime = System.currentTimeMillis();
	        HttpResponse response = client.execute(get);
	        HttpEntity entity = response.getEntity();
			if (entity!=null) {
				try {
					result = EntityUtils.toString(entity, charset);
				} catch (Exception e) {
					logger.error("读取数据出错",e);
				}finally{
					entity.getContent().close();
				}
			}
			long endTime = System.currentTimeMillis();
			logger.info("executeTime={}ms,url={}", new Object[] {endTime-startTime,
					get.getURI() });
			logger.debug("result={}", result);
		} catch (Exception e) {
			logger.error("请求失败",e);
		} 
		return result;
	}
	
	public GetRequest setReadTimeOut(int readTimeOut){
		if (readTimeOut>0) {
			this.readTimeOut = readTimeOut;
		}
		return this;
	}
	
	public GetRequest setConnectTimeOut(int connectTimeOut){
		if (connectTimeOut>0) {
			this.connectTimeOut = connectTimeOut;
		}
		return this;
	}

	public GetRequest setWaitTimeOut(int waitTimeOut){
		if (waitTimeOut>0) {
			this.waitTimeOut = waitTimeOut;
		}
		return this;
	}
}
