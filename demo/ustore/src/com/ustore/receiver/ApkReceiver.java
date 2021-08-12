package com.ustore.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jayqqaa12.abase.util.common.L;
import com.ustore.engine.PushEngine;

public class ApkReceiver extends BroadcastReceiver
{

	

	@Override
	public void onReceive(Context context, Intent intent)
	{
		L.i("有应用被安装" + intent.getDataString().split("package:")[1]);

		PushEngine.install(intent.getDataString().split("package:")[1]);
		PushEngine.pushInstall(context);
		
	}


}
