package com.keep.util;

import java.math.BigDecimal;

/**
 * 
 * @author lance
 * @2015年9月9日
 * @下午4:42:07
 * @TODO 格式化数据
 */
public class FormatNum {

	public  static double formDuber(double  d){
		if( d == 0) return 0;
		BigDecimal bg = new BigDecimal(d);  
        double f1 = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();  
		return f1;
		
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(formDuber(10*1D/3));
	}
}
