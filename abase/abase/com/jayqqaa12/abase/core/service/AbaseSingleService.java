package com.jayqqaa12.abase.core.service;

import com.jayqqaa12.abase.core.Abase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;

/**
 * 
 * 不重复的单任务 使用 
 * 多任务 建议使用 AbaseIntentService
 * 可能会 出现问题 比如 这个线程中又开一个线程 会失败
 * 这时候 请 使用 abaseService
 * 
 * @author jayqqaa12
 * @date 2013-5-14
 */
public abstract class AbaseSingleService extends Service implements Runnable
{
	protected abstract void doTask();

	@Override
	public void run()
	{
		Looper.prepare();
		doTask();
	}


	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Abase.init(this);
		
		new Thread(this).start();
	}
	
	



}
