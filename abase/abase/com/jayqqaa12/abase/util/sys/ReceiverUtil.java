package com.jayqqaa12.abase.util.sys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import com.jayqqaa12.abase.core.AbaseUtil;

/**
 * 提供各种 系统 广播的 动态 注册
 * 
 * @author jayqqaa12
 * @date 2013-5-15
 */
public class ReceiverUtil extends AbaseUtil
{

	
	/**
	 * 发送 sd卡 重新 挂载 广播 
	 * 用来刷新 文件
	 * @param context
	 * @return
	 */
	public static void sendSdMounted(Context context)
	{
		Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED,Uri.parse("file://"+Environment.getExternalStorageDirectory()));
		context.sendBroadcast(intent);
	}
	
	
	/**
	 * 发送 释放 内存的广播 
	 * 
	 * 内存 不足时 可以使用
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static void sendKillActivityAction(Context context)
	{
		Intent intent = new Intent();
		intent.setAction("kill_activity_action");
		context.sendBroadcast(intent);
	}
	
	/**
	 *  电量 变化的 广播
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static BroadcastReceiver registBatteryChange(Context context,BroadcastReceiver receiver)
	{
		return registReceiver(context, receiver, Intent.ACTION_BATTERY_CHANGED);
	}
	
	/**
	 * 收短信的 广播
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static BroadcastReceiver registSmsReceived(Context context, BroadcastReceiver receiver)
	{
		 return registReceiver(context, receiver, "android.provider.Telephony.SMS_RECEIVED");
	}

	/**
	 * 定时 发送的 广播 每分钟一次
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static BroadcastReceiver registTimeTick(Context context, BroadcastReceiver receiver)
	{
		return registReceiver(context, receiver, Intent.ACTION_TIME_TICK);
	}

	/**
	 * 卸载的 广播
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static BroadcastReceiver registPackageRemoved(Context context, BroadcastReceiver receiver)
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
		intentFilter.addDataScheme("package");
		return registReceiver(context, receiver, intentFilter);

	}
	
	/**
	 * 有新安装的程序的 广播
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static BroadcastReceiver registPackageAdded(Context context, BroadcastReceiver receiver)
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.ACTION_PACKAGE_ADDED");
		intentFilter.addDataScheme("package");
		return registReceiver(context, receiver, intentFilter);
	}
	
	/**
	 * 有 程序被 更新的 广播
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static BroadcastReceiver registPackageReplaced(Context context, BroadcastReceiver receiver)
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.ACTION_PACKAGE_REPLACED");
		intentFilter.addDataScheme("package");
		return registReceiver(context, receiver, intentFilter);
	}
	
	

	public static BroadcastReceiver registReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter)
	{
		filter.setPriority(Integer.MAX_VALUE);
		context.registerReceiver(receiver, filter);
		return receiver;
	}

	public static BroadcastReceiver registReceiver(Context context, BroadcastReceiver receiver, String action)
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(action);
		filter.setPriority(Integer.MAX_VALUE);
		context.registerReceiver(receiver, filter);

		return receiver;
	}
	
	

}
