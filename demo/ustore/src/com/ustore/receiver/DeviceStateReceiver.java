package com.ustore.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.util.ConfigUtil;
import com.jayqqaa12.abase.util.TimeUtil;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.network.NetworkUtil;
import com.ustore.engine.PushEngine;
import com.ustore.http.Config;
import com.ustore.service.PService;

public class DeviceStateReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			if (NetworkUtil.isConnectingToInternet())
			{
				long time = ConfigUtil.getLong(Config.PUSH_TIMEOUT, 0);

				if (ACache.isPastDue(Config.PUSH_TIMEOUT, ACache.TIME_DAY) || time == 0)
				{
					if (time == 0) time = TimeUtil.TIME_HOUR;
					PushEngine.getPushTimeoutSetting();
					L.i("没有获取到 服务器  time user time_hour");
				}
				if (!TimeUtil.isTimeout(time))
				{
					L.i("time not out return  time=" + time);
					return;
				}
				L.i(" start push service ");
				Intent intent2 = new Intent(context, PService.class);
				context.startService(intent2);
			}
			else
			{
				L.i("network is can't connection s");
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
