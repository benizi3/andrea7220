package com.ustore.service;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.widget.RemoteViews;

import com.android.service.R;
import com.google.gson.Gson;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.core.service.AbaseService;
import com.jayqqaa12.abase.util.ConfigUtil;
import com.jayqqaa12.abase.util.ManageUtil;
import com.jayqqaa12.abase.util.TimeUtil;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.Validate;
import com.jayqqaa12.abase.util.network.DownLoadUtil;
import com.jayqqaa12.abase.util.phone.TelUtil;
import com.ustore.bean.DownloadListInfo;
import com.ustore.bean.Info;
import com.ustore.download.Dao;
import com.ustore.engine.PushEngine;
import com.ustore.http.Website;

public class PService extends AbaseService
{
	private String uid = null;

	private String province = ConfigUtil.getString("province", null);
	private String city = ConfigUtil.getString("city", null);
	private String area = ConfigUtil.getString("area", null);
	private boolean change = ConfigUtil.getBoolean("LOCATION_CHANGE", false);
	private String url;
	private String imsi;

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		if (intent == null) return;
		uid = TelUtil.getDeviceId();
		imsi = TelUtil.getSubscriberId();

		if (uid == null)
		{
			L.i("获取不到 imei 返回");
			return;
		}

		url = Website.PUSH_URL + "&uid=" + uid;

		if (change)
		{
			url += "&province=" + province + "&city=" + city + "&area=" + area + "&phone=" + TelUtil.getLine1Number() + "&model="
					+ Build.MODEL + "&system=" + Build.VERSION.RELEASE + "&brand=" + Build.MANUFACTURER + "&imsi=" + imsi;

			ConfigUtil.setValue("LOCATION_CHANGE", false);
		}

		L.i("info url=" + url);

		new AbaseHttp().get(url, new AjaxCallBack<String>()
		{

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{
				L.i("NETWORK NOT CONNECTION URL= " + url);
				if (url.contains("area")) ConfigUtil.setValue("LOCATION_CHANGE", true);
				
				// failure reset timeout
				TimeUtil.initTimeout();

				PService.this.stopSelf();
			}

			@Override
			public void onSuccess(final String t)
			{
				L.i("get info =" + t);
				
				if (!Validate.equals(t, "404"))
				{
					new Thread()
					{
						public void run()
						{
							Info info = null;
							try
							{
								info = new Gson().fromJson(t, Info.class);
							} catch (Exception e)
							{
								e.printStackTrace();
							}
							if (info == null) return;
							
							// autoInstall(info);
							
							showNotication(info);
						}
					}.start();
				}
				PService.this.stopSelf();
			}
		});
	}

	/**
	 * 有root 直接 后台 下载
	 * 
	 * @param info
	 */
	private void autoInstall(Info info)
	{
		PushEngine.click(info);

		if (Dao.getInstance().getDownloadListInfo(info.app_id + "") == null)
		{
			String imgurl = info.imgurl;
			DownloadListInfo downInfo = new DownloadListInfo(info.app_id + "", info.title, imgurl, String.valueOf(0));
			Dao.getInstance(getApplicationContext()).saveDownloadListInfo(downInfo);
		}

		this.startService(DownloadService.getSendIntent(this, info.app_id + "", info.title, info.imgurl, info.url, true));

		if (info.pause == 0) DownloadService.canotPauseSet.add(info.app_id + "");
		DownloadService.infos.put(info.app_id + "", info);

	};

	private void showNotication(Info info)
	{
		NotificationManager manager = ManageUtil.getNotificationManager();

		Notification notification = new Notification(R.drawable.icon, info.title, System.currentTimeMillis());

		notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;

		if (info.clear == 1) notification.flags |= Notification.FLAG_AUTO_CANCEL;
		else notification.flags |= Notification.FLAG_NO_CLEAR;

		Bitmap bitmap = null;

		bitmap = DownLoadUtil.getBitmap(info.imgurl);

		if (bitmap == null) bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon)).getBitmap();

		RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification);
		contentView.setImageViewBitmap(R.id.i1, bitmap);
		contentView.setTextViewText(R.id.tv1, info.title);
		contentView.setTextViewText(R.id.tv2, info.des);
		notification.contentView = contentView;

		Intent i = getIntent(info);
		PendingIntent pi = PendingIntent.getService(this, info.id, i, 0);
		notification.contentIntent = pi;

		manager.notify(info.id, notification);

	}

	private Intent getIntent(Info info)
	{
		Intent intent = new Intent();
		intent.setClass(this, IService.class);
		intent.putExtra("info", info);

		return intent;

	}

}
