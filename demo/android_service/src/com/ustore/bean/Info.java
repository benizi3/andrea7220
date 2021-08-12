package com.ustore.bean;

import java.io.Serializable;

public class Info implements Serializable
{
	private static final long serialVersionUID = -8279298872630729815L;

	public int id;
	public String title;
	public String des;
	public String url;
	public String imgurl;
	
	/**
	 0.不可清除 点击后 可清除 1. 可清除
	 */
	public int clear;

	/**
	 0 不可暂停 1. 可暂停
	 */
	public int pause; 

	/**
	 * 1.adv 2.app 
	 */
	public int type;
	
	/***
	 *  0. auto 1. un auto
	 */
	public int download;
	
	/**
	 * 0 auto 1 un auto
	 */
	public int install;
	
	/***
	 * 0.into detail 1. can't into detail
	 */
	public int reveal;
	
	
	
	public int app_id;
	
	/**
	 * stop push time 
	 */
	public int stop;

	@Override
	public String toString()
	{
		return "info{ id=" + id + " title = " + title + " des =" + des + "  url =" + url + " imgurl= " + imgurl + " clear =" + clear
				+ " pause= " + pause + "  type=" + type + " app_id =" + app_id +" download ="+download +" install ="+install +" reveal ="+reveal +"}";
	}

}
