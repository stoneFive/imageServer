package com.keep.util.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.keep.util.http.InfoCode;

/**
 * 系统及业务异常类
 * Created by huiwu on 2014/9/20.
 */
public class AppException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1322392158514616169L;
	private InfoCode infoCode;

    public AppException(){
        this.infoCode = InfoCode.SERVICE_UNAVAILABLE;
    }
    public AppException(String msg){
        super(msg);
        this.infoCode = InfoCode.SERVICE_UNAVAILABLE;
    }
    public AppException(InfoCode infoCode){
        this.infoCode = infoCode;
    }
    public AppException(InfoCode infoCode,String msg){
        super(msg);
        this.infoCode = infoCode;
    }

    public InfoCode getInfoCode() {
        return infoCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
