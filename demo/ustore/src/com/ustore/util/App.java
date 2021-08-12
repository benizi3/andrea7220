package com.ustore.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayqqaa12.abase.core.AbaseApp;
import com.jayqqaa12.abase.util.common.L;
import com.ustore.bean.ApkInfo;
import com.ustore.engine.InitEngine;

public class App extends AbaseApp implements Thread.UncaughtExceptionHandler
{

	/**
	 * 记录需要更新的apk
	 */
	ArrayList<ApkInfo> apks = new ArrayList<ApkInfo>();

	@Override
	public void onCreate()
	{
		L.i("app create");
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(this);
		try
		{
			new InitEngine().init(this);
		} catch (Exception e)
		{}
		

	}

	/**
	 * 添加需要更新的apk
	 * 
	 * @param info
	 */
	public void addUpdateApk(ApkInfo info)
	{
		apks.add(info);
	}

	/**
	 * 清空需要更新的apk
	 */
	public void clearUpdateApk()
	{
		apks.clear();
	}

	public int getUpdateApkCount()
	{
		return apks.size();
	}

	public ArrayList<ApkInfo> getApkList()
	{
		return apks;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		ex.printStackTrace();
		L.i("uncaughtException =" + ex.getMessage());
		System.exit(0);

	}

}
