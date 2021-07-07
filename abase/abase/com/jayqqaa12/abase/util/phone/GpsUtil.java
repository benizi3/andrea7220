package com.jayqqaa12.abase.util.phone;

import java.util.List;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;

import com.jayqqaa12.abase.core.AbaseUtil;
import com.jayqqaa12.abase.util.ManageUtil;

/**
 * 
 * GPS 目前 不是很好的方案了 因为在 室内 不容易 获取到 信号~
 * 
 * @author 12
 *
 */
public class GpsUtil extends AbaseUtil
{

	/**
	 * 是否 已经 打开 gps
	 * 
	 * @param manager
	 * @return
	 */
	public static boolean isOpenGPS(LocationManager manager)
	{

		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	/**
	 * 设置 一个 监听器 获得 位置信息 应该为单例
	 * 
	 * @param listener
	 * @throws Exception
	 */
	public static synchronized void getLocation(LocationListener listener)
	{
		openGPS();

		LocationManager manager = ManageUtil.getLocationManager();

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setSpeedRequired(true);
		criteria.setCostAllowed(true);
		String provider = manager.getBestProvider(criteria, true);

		if (provider == null)
		{
			// 如果 provider gps 找不到 只好随便给 它一个
			List<String> providers = manager.getProviders(true);
			provider = providers.get(0);
		}

		manager.requestLocationUpdates(provider, 60000, 50, listener);
	}

	
	
	
	/**
	 * 打开 GPS 
	 */
	public static void openGPS()
	{
		Context context = getContext();
		if (!ManageUtil.getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			Intent GPSIntent = new Intent();
			GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
			GPSIntent.setData(Uri.parse("custom:3"));
			try
			{
				PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
			}
			catch (CanceledException e)
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * 中止 监听器
	 * 
	 * @param listener
	 */
	public static void stopGPSListener(LocationListener listener)
	{
		if (listener != null) ManageUtil.getLocationManager().removeUpdates(listener);
	}

}
