package com.keep.util.http;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by huiwu on 2014/9/20.
 */
public class InfoCode implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4181865758935309876L;
	private int code;
    private String msg;

    private InfoCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static final InfoCode SUCCESS = new InfoCode(0, "成功");
    public static final InfoCode FAILD = new InfoCode(1, "");
    public static final InfoCode PARAM_IS_NULL = new InfoCode(1, "表单参数为空！！！");

    /**
     * *系统级错误开始***
     */
    public static final InfoCode SERVICE_UNAVAILABLE = new InfoCode(10000, "系统错误");

    public static final InfoCode UNAUTHORIZED = new InfoCode(20000, "用户没有取得授权或授权已过期");

    /****系统级错误结束****/

    /****参数错误开始****/
    public static final InfoCode USERNAME_PASSWORD_IS_NULL = new InfoCode(40000, "用户名或密码为空");

    public static final InfoCode INVALID_USERNAME_PASSWORD = new InfoCode(40001, "用户名或密码不正确");

    public static final InfoCode NOTFOUND_USER = new InfoCode(40002, "用户不存在");

    public static final InfoCode DISABLE_USER = new InfoCode(40003, "此用户被禁用");
    /****参数错误结束****/

    /****业务错误开始****/

    /****业务错误结束****/

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
