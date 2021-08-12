package com.ustore.service;

import android.content.Intent;

import com.jayqqaa12.abase.core.service.AbaseService;
import com.jayqqaa12.abase.util.ManageUtil;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.sys.SysIntentUtil;
import com.ustore.activity.DownDetails;
import com.ustore.activity.Home;
import com.ustore.bean.DownloadListInfo;
import com.ustore.download.Dao;
import com.ustore.engine.PushEngine;
import com.ustore.model.Info;

public class IService extends AbaseService
{

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		if (intent == null) return;
		Info info = (Info) intent.getSerializableExtra("info");
		if (info == null) return;

		// 1, adv
		switch (info.type)
		{
		case 1:
			adv(info);
			break;
		// 2 app
		case 2:
			// first 判断 是否要
			if (info.reveal == 0)
			{
				// 如果是自动下载 马上下载
				if (info.download == 0) autoApp(info);
				app(info);
			}
			else autoApp(info);
			saveInfo(info);
			
			break;
		}

		PushEngine.click(info);
		ManageUtil.getNotificationManager().cancel(info.id);

		this.stopSelf();

	}

	private void saveInfo(Info info)
	{
		if (info.pause == 0) DownloadService.canotPauseSet.add(info.app_id + "");

		DownloadService.infos.put(info.app_id + "", info);
	}

	/**
	 * 点击后自动下载 应用
	 * 
	 * @param info
	 */
	private void autoApp(Info info)
	{
		L.i("auto download app send intent ");
		Home.isDownLoading = true;
		if (Dao.getInstance().getDownloadListInfo(info.app_id + "") == null)
		{
			String imgurl = info.imgurl;
			DownloadListInfo downInfo = new DownloadListInfo(info.app_id + "", info.title, imgurl, String.valueOf(0));
			Dao.getInstance(getApplicationContext()).saveDownloadListInfo(downInfo);
		}

		this.startService(DownloadService.getSendIntent(this, info.app_id + "", info.title, info.imgurl, info.url, true));
	}

	/**
	 * 点击后 进入 应用详情 界面
	 * 
	 * @param info
	 */
	private void app(Info info)
	{


		Intent intent = new Intent(this, DownDetails.class);
		intent.putExtra("id", info.app_id + "");
		intent.putExtra("url", info.url);
		intent.putExtra("info_id", info.id);
		intent.putExtra("auto", false);
		intent.putExtra("info", info);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);

	}

	private void adv(Info info)
	{
		SysIntentUtil.goWeb(this, info.url);

	}

}
