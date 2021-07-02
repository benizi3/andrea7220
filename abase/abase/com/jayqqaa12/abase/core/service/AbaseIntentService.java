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
 *  多个任务 或者要重新 开始多次的任务 都 放在这里
 *  
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
