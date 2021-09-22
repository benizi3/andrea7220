package com.jayqqaa12.abase.kit.common;


import java.util.Collection;

/**
 * 校验工具类
 */
public class Validate {
	
	
			
	/**
	 * equals
	 * @param str
	 * @return
	 */
	public static boolean equals (String s1,String s2)
	{
		return notEmpty(s1)&&notEmpty(s2) &&s1.equals(s2);
		
	}
	
	

	/**
	 * 验证字符串有效性
	 */
	public static boolean isEmpty(String str){
		
		return !notEmpty(str);
	}
	
	
	
	/**
	 * 验证字符串有效性
	 */
	public static boolean notEmpty(String str){
		if(str == null || "".equals(str.trim())){
			return false ;
		}
		return true ;
	}
	
	/**
	 * 验证集合有效性
	 */
	public static boolean notEmpty(Collection col){
		if(col == null || col.isEmpty()){
			return false ;
		}
		return true ;
	}
	
	/**
	 * 验证数组有效性
	 */
	public static boolean notEmpty(Object[] arr){
		if(arr == null || arr.length == 0){
			return false ;
		}
		return true ;
	}
	
	
	
}