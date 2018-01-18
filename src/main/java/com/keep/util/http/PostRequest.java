package com.keep.util.http;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理post请求
 */
public final class PostRequest extends Request{

	private static final Logger logger = LoggerFactory
			.getLogger(PostRequest.class);

	private HttpPost post = null;

	private HttpClient client = null;
	
	private HttpEntity httpEntity = null;

	private final List<NameValuePair> params = new ArrayList<NameValuePair>();

	private static final String DEFAULTCHARSET = "UTF-8";

	private String charset = DEFAULTCHARSET;

	public PostRequest(HttpClient client, String url) {
		this.client = client;
		post = new HttpPost(url);
		post.setConfig(getRequestConfig());
	}

	public PostRequest(HttpClient httpClient, String url,
			String charset) {
		this(httpClient,url);
		this.charset = charset;
	}

	public PostRequest addHeader(String name, Object value) {
		if (value == null) {
			return this;
		}
		post.addHeader(name, String.valueOf(value));
		return this;
	}

	public PostRequest addParam(String name, Object value) {
		if (value == null) {
			return this;
		}
		params.add(new BasicNameValuePair(name, String.valueOf(value)));
		return this;
	}
	
	
	/**
	 * 设置body后，通过addParam方法添加的参数将不会设置到请求中
	 * @param body
	 * @return
	 */
	public PostRequest setBody(String body) {
		try {
			if (body==null) {
				return this;
			}
			httpEntity = new StringEntity(body,charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * 设置body后，通过addParam方法添加的参数将不会设置到请求中
	 * @param body
	 * @return
	 */
	public PostRequest setBody(File body,ContentType contextType) {
		try {
			if (body==null||body.exists()) {
				return this;
			}
			httpEntity = new FileEntity(body,contextType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	/**
	 * 设置body后，通过addParam方法添加的参数将不会设置到请求中
	 * @param body
	 * @return
	 */
	public PostRequest setBody(byte[] body,ContentType contextType) {
		try {
			if (body==null) {
				return this;
			}
			httpEntity = new ByteArrayEntity(body,contextType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	/**
	 * 设置body后，通过addParam方法添加的参数将不会设置到请求中
	 * @param body
	 * @return
	 */
	public PostRequest setBody(InputStream body) {
		try {
			if (body==null) {
				return this;
			}
			httpEntity = new InputStreamEntity(body);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	
	
	public String execute() {
		String result = null;
		try {
			long startTime = System.currentTimeMillis();
			if (httpEntity==null) {
				httpEntity = new UrlEncodedFormEntity(params,charset);
			}
			post.setEntity(httpEntity);
			HttpResponse response = client.execute(post);
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
					post.getURI() });
			logger.debug("result={}", result);
			return result;
		} catch (Exception e) {
			logger.error("请求失败", e);
		}

		return null;
	}
	
	
	public PostRequest setReadTimeOut(int readTimeOut){
		if (readTimeOut>0) {
			this.readTimeOut = readTimeOut;
		}
		return this;
	}
	
	public PostRequest setConnectTimeOut(int connectTimeOut){
		if (connectTimeOut>0) {
			this.connectTimeOut = connectTimeOut;
		}
		return this;
	}
	
	public PostRequest setWaitTimeOut(int waitTimeOut){
		if (waitTimeOut>0) {
			this.waitTimeOut = waitTimeOut;
		}
		return this;
	}

}
