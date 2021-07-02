package com.jayqqaa12.abase.core.service;

import com.jayqqaa12.abase.core.Abase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * 这个 类 就是 增加 了 ioc
 * @author  jayqqaa12
 *
 */
public class AbaseService extends Service
{

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	
	public void onCreate() {
		Abase.init(this);
	};

}
