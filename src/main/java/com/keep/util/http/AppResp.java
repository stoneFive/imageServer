package com.keep.util.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by huiwu on 2014/9/20.
 */
@JSONType(orders = { "status", "msg", "data" })
public class AppResp implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3942357976595852781L;

	private int status;

    private String msg;

    private Object data;

    private AppResp(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    private AppResp(int status,String msg,Object data){
        this(status,msg);
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public AppResp(InfoCode infoCode){
        this(infoCode.getCode(),infoCode.getMsg());
    }

    public AppResp(InfoCode infoCode,Object data){
        this(infoCode.getCode(),infoCode.getMsg(),data);
    }

    @Override
    public String toString() {
    	 return JSON.toJSONString(this);
    }
}
