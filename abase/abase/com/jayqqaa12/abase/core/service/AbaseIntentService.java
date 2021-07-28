package com.jayqqaa12.abase.core.service;

import android.app.IntentService;
import android.content.Context;

import com.jayqqaa12.abase.util.common.TAG;

/**
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



	protected Context getContext()
	{
		return getApplicationContext();
	}

}
