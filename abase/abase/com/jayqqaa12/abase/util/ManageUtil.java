package com.jayqqaa12.abase.util;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.jayqqaa12.abase.core.AbaseUtil;

public class ManageUtil extends AbaseUtil
{

	public static DevicePolicyManager getDevicePolicyManager()
	{
		return (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
	}

	public static KeyguardManager getKeyguardManager()
	{
		return (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
	}

	public static void registAdminDevice(Context context, Class<? extends DeviceAdminReceiver> admin)
	{
		DevicePolicyManager manager = ManageUtil.getDevicePolicyManager();
		ComponentName adminName = new ComponentName(context, admin);
		// is exist admin active ?
		if (!manager.isAdminActive(adminName))
		{
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminName);
			context.startActivity(intent);
		}

	}

	public static LocationManager getLocationManager()
	{
		return (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
	}

	public static WindowManager getWindowManager()
	{

		return (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
	}

	public static ActivityManager getActivityManager()
	{
		return (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
	}

	public static NotificationManager getNotificationManager()
	{
		return (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static PackageManager getPackManager()
	{

		return getContext().getPackageManager();
	}

	public static ConnectivityManager getConnectivtyManager()
	{
		return (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static PowerManager getPowerManager()
	{
		return (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
	}

	public static TelephonyManager getTelephonyManager()
	{
		return (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
	}

	public static WifiManager getWifiManger()
	{
		return (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
	}
	

	public static LayoutInflater getInflater()
	{
		return  (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

}
