package com.keep.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMethod;

public class HttpUtils {
	
	public static String sendHttp(String urls,Map<String,String> param) throws Exception{
		URL url = new URL(urls);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod(RequestMethod.POST.toString());
		http.setRequestProperty("Charset", "utf-8");
		http.setDoOutput(true);// 是否输入参数
		
		if(param!=null && param.size()>0){
			StringBuffer params = new StringBuffer();
			Set<String> paramName = param.keySet();
			Iterator<String> it = paramName.iterator();
			while(it.hasNext()){
				String name = it.next();
				String value = param.get(name);
				params.append(name+"="+value+"&");
			}
			byte[] bypes = params.toString().getBytes();
			http.getOutputStream().write(bypes);// 输入参数
		}
		
		
		InputStream inStream=http.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream,"utf-8"));
		String line = "";
        String result = "";
        while(null != (line=br.readLine())){
            result += line;
        }
        return result;
	}

}
