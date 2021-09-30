package com.jayqqaa12.abase.core.listener;

/**
 * 网络连接的时候的反馈接口
 * @author 12
 *
 */
public interface Conn
{
	
	/***
	 * 传递 status  在setDate的时候 根据status 来 刷新或初始化数据
	 * @param url
	 * @param cacheTime
	 * @param status
	 */
	public void connect(String url,int cacheTime,int status);
	
	/**
	 * 一般为json数据
	 * @param data
	 * @param status
	 */
	public void setDate(String data,int status);

}
