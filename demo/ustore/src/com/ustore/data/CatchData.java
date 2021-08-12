package com.ustore.data;


/**
 *保存listView列表要显示的数据
 */
public class CatchData {
	public String id;
	public String url;
	public String name;
	public String icon;
	public String num;
	
	public String version;
	
	/**
	 * 文件大小
	 */
	public String size;
	
	/**
	 * 下载次数
	 */
	public String downloadTimes;
	
	/**
	 * 
	 * state=0  下载
	 * state=1  启动
	 * state=2  更新
	 * state=3  暂停
	 */
	public int state=0;
	
}
