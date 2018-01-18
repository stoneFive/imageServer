package com.keep.util.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.keep.util.http.InfoCode;


/**
 * 未授权异常
 * Created by huiwu on 2014/9/20.
 */
public class UnauthorizedException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7285450318547800963L;
	private InfoCode infoCode;

    public UnauthorizedException(){
        this.infoCode = InfoCode.UNAUTHORIZED;
    }

    public InfoCode getInfoCode() {
        return infoCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
