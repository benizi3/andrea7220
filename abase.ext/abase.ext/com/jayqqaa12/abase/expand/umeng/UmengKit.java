package com.jayqqaa12.abase.expand.umeng;

import android.content.Context;
import android.text.TextUtils;

import com.jayqqaa12.abase.core.Abase;
import com.jayqqaa12.abase.kit.ManageKit;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class UmengKit
{

	
	/**
	 * 获取测试信息
	 * @param context
	 * @return
	 */
	public static String getDeviceInfo(Context context)
	{
		try
		{
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = tm.getDeviceId();
			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) device_id = mac;

			if (TextUtils.isEmpty(device_id))
			{
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 进入反馈界面
	 */
	public static void intoFeedBack(Context context){
		
		FeedbackAgent agent = new FeedbackAgent(context);
	    agent.startFeedbackActivity();
	}
	
	
	/**
	 * 自动更新  默认wifi 下
	 * @param context
	 */
	public static void autoUpdate( ){
		
		UmengUpdateAgent.update(Abase.getContext());
		
	}

	
	/***
	 * 手动 检测更新 无视 网络环境
	 */
	public static void checkUpdate(){
		UmengUpdateAgent.forceUpdate(Abase.getContext());
	}
	
	
	/**
	 * 静默下载 下载后 通知栏提示 
	 * 
	 * 默认 wifi 环境
	 */
	public static void silentUpdate(){
		
		UmengUpdateAgent.silentUpdate(Abase.getContext());
	}
	
	
	
}
