package com.jayqqaa12.reader.model;

import java.io.Serializable;

public class Book implements Serializable
{
	private static final long serialVersionUID = 3818662966565810856L;

	public int id;
	public String name="";
	public String des="";
	
	public String path="";
	
	/**
	 * 看的进度
	 */
	public long progress;
	
	/**
	 * 总大小
	 */
	public long total;
	
	/**
	 * 封面
	 */
	public int icon;

}
