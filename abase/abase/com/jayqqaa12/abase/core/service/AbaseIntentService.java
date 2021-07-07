package com.jayqqaa12.abase.core.service;

import com.jayqqaa12.abase.core.Abase;
import com.jayqqaa12.abase.util.common.TAG;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * 
 *  This's IntentService  ioc
 *  
 * WANING  如果 多线程 任务 不能 放在 这里 
 * 这里 适合 可以不需要并行的 任务   每个 请求会 放入 工作线程 依次 执行 而不是并行的
 * 
 */
public abstract class AbaseIntentService extends IntentService
{

	public AbaseIntentService()
	{
		super(TAG.SERVICE);
	}
	
	public AbaseIntentService(String threadName)
	{
		super(threadName);
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		Abase.init(this);
		
	}

	protected Context getContext()
	{
		return getApplicationContext();
	}

}
