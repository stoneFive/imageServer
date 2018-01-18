package com.keep.util.http;

import java.nio.charset.CodingErrorAction;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 创建Http请求工具类
 * @author huiwu
 *
 */
public class HttpClientUtil {

	private static PoolingHttpClientConnectionManager connectionManager = null;
	
	// 最大连接数
	private static final int MAX_TOTAL_CONNECTIONS = 500;
	// 每个路由最大连接数
	private static final int MAX_ROUTE_CONNECTIONS = 50;
	
	/**
	 * 初始化配置
	 */
	static{
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https",(new SSLConnectionSocketFactory(SSLContexts.createDefault(),SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER))).build();
		connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setMalformedInputAction(CodingErrorAction.IGNORE)
				.setUnmappableInputAction(CodingErrorAction.IGNORE)
				.setCharset(Consts.UTF_8).build();
		connectionManager.setDefaultConnectionConfig(connectionConfig);
		connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
	}
	
	/**创建 POST请求 **/
	public static PostRequest buildPostRequest(String url){
		return new PostRequest(getHttpClient(), url);
	}
	
	public static PostRequest buildPostRequest(String url,String charset){
		return new PostRequest(getHttpClient(), url,charset);
	}
	
	public static GetRequest buildGetRequest(String url){
		return new GetRequest(getHttpClient(), url);
	}
	
	public static GetRequest buildGetRequest(String url,String charset){
		return new GetRequest(getHttpClient(), url,charset);
	}
	
	private static HttpClient getHttpClient(){
		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}
	
}


