package com.ustore.bean;

import android.graphics.drawable.Drawable;

public class HomeGuideBean {

	/**
	 * 导航图
	 */
	public String imgurl;
	
	/**
	 * type =1链接   2专题  3详情
	 */
	public int type;
	
	/**
	 * 导航数据  ，  type=1,数据为链接网址，  type=2,数据为专题id   type=3   为apk id
	 */
	public String data;
	
	/**
	 * type=2时,专题的标题和描述
	 */
	public String title;
	public String desc;
	
	/**
	 * type ==3  apk download url
	 */
	public String url;

}
