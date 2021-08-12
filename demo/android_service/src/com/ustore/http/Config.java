package com.ustore.http;

import com.jayqqaa12.abase.util.ResUtil;
import com.jayqqaa12.abase.util.sys.RootUtil;


public class Config
{
	/***
	 * 设置的 浏览器 主页  
	 */
	public static final String HOME_PAGE ="home_page";
	
	
	/***
	 * 是否 开启设置 主页功能
	 */
	public static final String HOME_PAGE_OPEN ="home_page_open";
	
	/***
	 * flag 每天开启一次主页的 标识符
	 */
	public static final String HOME_PAGE_OPEN_TIME ="home_page_open_time";
	
	public static final String   PUSH_USER_INSTALL_APP ="PUSH_USER_INSTALL_APP" ;
	
	public static final String IS_INIT = "is_init";
	public static final String PUSH_TIMEOUT = "push_time_out";
	
	public static final String HOME_PAGE_PROVIDEE_AUTHORITIES ="content://com.ustore.browser.homepage";
	
	public static final boolean  ROOT = RootUtil.getRootAhth();
	
	public static final boolean ENCODE=false;

	public static final String HOME_PAGE_RUNNING = "home_page_running";

	public static final String TIMEOUT_RUNNING = "time_out_running";


	public static final int PUSH_APP_TYPE=0;





}
