package com.jayqqaa12.abase.core.service;

import com.jayqqaa12.abase.core.Abase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * 这个 类 就是 增加 了 ioc
 * 
 * 把 需要在初始化 做的或者 只执行一次的任务 事情 写在 doCreate 方法中就可以了
 * 
 * 把需要 重复做的 任务写在 doStart 方法中即可
 * @author  jayqqaa12
 *
 */
public abstract class AbaseService extends Service
{
	

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	

	public void onCreate() {
		Abase.init(this);
	}

}
