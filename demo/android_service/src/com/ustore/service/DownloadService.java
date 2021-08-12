package com.ustore.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.RemoteViews;

import com.android.service.R;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.core.service.AbaseService;
import com.jayqqaa12.abase.util.ManageUtil;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.T;
import com.jayqqaa12.abase.util.common.Validate;
import com.jayqqaa12.abase.util.network.DownLoadUtil;
import com.jayqqaa12.abase.util.network.NetworkUtil;
import com.jayqqaa12.abase.util.phone.TelUtil;
import com.jayqqaa12.abase.util.sys.RootUtil;
import com.jayqqaa12.abase.util.sys.SdCardUtil;
import com.jayqqaa12.abase.util.sys.SysIntentUtil;
import com.ustore.activity.DownDetails;
import com.ustore.bean.DownloadListInfo;
import com.ustore.bean.Info;
import com.ustore.download.Dao;
import com.ustore.download.DownloadInfo;
import com.ustore.engine.PushEngine;
import com.ustore.http.Config;
import com.ustore.http.Website;

public class DownloadService extends AbaseService
{

	private static Dao dao = Dao.getInstance();
	/**
	 * 保存 当前 正在 下载的 id
	 */
	public static Set<String> downloadingSet = Collections.synchronizedSet(new HashSet<String>());
	/**
	 * 保存在 下载 队列中 的id
	 */
	public static Set<String> downloadSet = Collections.synchronizedSet(new HashSet<String>());
	/**
	 * 保存在 禁止暂停 队列 的 id
	 */
	public static Set<String> canotPauseSet = Collections.synchronizedSet(new HashSet<String>());

	/***
	 * 保存下载文件的大小
	 */
	public static Map<String, Long> downloadSize = Collections.synchronizedMap(new HashMap<String, Long>());

	/**
	 * 保存对任务的操作
	 */
	public static Map<String, HttpHandler<File>> handlers = Collections.synchronizedMap(new HashMap<String, HttpHandler<File>>());

	/**
	 * 保存 推送 PUSH info 方便 下载 完成 以后 继续 数据 推送
	 */
	public static Map<String, Info> infos = Collections.synchronizedMap(new HashMap<String, Info>());

	/***
	 * 保存更新 进度的 notification
	 */
	public volatile static Map<String, Notification> notifcaitions = new HashMap<String, Notification>();
	private BroadcastReceiver receiver;

	private void sendBroadcast(final int status, final String id)
	{
		Intent intent = new Intent("com.ustore.download.update");
		intent.putExtra("status", status);
		intent.putExtra("id", id);
		DownloadService.this.sendBroadcast(intent);
	}

	@Override
	public void onCreate()
	{
		receiver = new UpdateNoticationReceiver();
		this.registerReceiver(receiver, new IntentFilter("com.ustore.download.notication"));
	}

	private class UpdateNoticationReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String id = intent.getStringExtra("id");
			final String name = intent.getStringExtra("name");
			final String url = intent.getStringExtra("url");
			final String imgurl = intent.getStringExtra("imgurl");
			final boolean pause = intent.getBooleanExtra("pause", false);

			/***
			 * 这里 是notication 的暂停按钮点击后的结果
			 */
			if (Validate.notEmpty(id))
			{
				Notification notion = notifcaitions.get(id);
				if (notion != null)
				{
					if (downloadingSet.contains(id) || pause)
					{
						if (canotPauseSet.contains(id))
						{
							T.ShortToast(R.string.download_not_pause);
							return;
						}
						stopTaskNotStopNotifiction(id);
						sendBroadcast(0, id);
						RemoteViews contentView = notion.contentView;
						contentView.setImageViewResource(R.id.iv_bt, R.drawable.down);
						contentView.setTextViewText(R.id.tv_bt_text, getText(R.string.download));
						ManageUtil.getNotificationManager().notify(Integer.parseInt(id), notion);
						return;
					}
					else
					{
						RemoteViews contentView = notion.contentView;
						contentView.setImageViewResource(R.id.iv_bt, R.drawable.pause);
						contentView.setTextViewText(R.id.tv_bt_text, getText(R.string.pause));
						ManageUtil.getNotificationManager().notify(Integer.parseInt(id), notion);

						if (Dao.getInstance().getDownloadListInfo(id) == null)
						{
							DownloadListInfo downInfo = new DownloadListInfo(id, name, imgurl, String.valueOf(0));
							Dao.getInstance(getApplicationContext()).saveDownloadListInfo(downInfo);
						}

						startService(getSendIntent(context, id, name, imgurl, url, false));
					}
				}
			}

		}

	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		if (intent == null) return;

		File f = new File(Website.sd_path + "download");
		if (!f.exists()) f.mkdirs();

		final String id = intent.getStringExtra("id");
		final String name = intent.getStringExtra("name");
		final String url = intent.getStringExtra("url");
		final String imgurl = intent.getStringExtra("imgurl");

		final String downloadPath = Website.sd_path + "download/" + id + ".apk";
		final boolean pause = intent.getBooleanExtra("pause", false);

		if (downloadingSet.contains(id) || downloadSet.contains(id))
		{
			L.i("alreay at download queen return ");
			return;
		}
		downloadSet.add(id);
		sendBroadcast(3, id);

		// if 没有 info
		if (dao.notHasInfors(id))
		{
			DownloadInfo info = new DownloadInfo(id, 0, 0, 0, url, 0, downloadPath);
			dao.saveInfo(info);
		}

		if (!SdCardUtil.isCanUseSdCard())
		{
			T.ShortToast(R.string.download_sd_not_found);
			sendBroadcast(0, id);
			stopTask(id);
			return;
		}

		if (SdCardUtil.getAvailableSDRom() * 1024 < 50)
		{
			T.ShortToast(R.string.download_sd_space);
			sendBroadcast(0, id);
			stopTask(id);
			return;
		}
		if(!NetworkUtil.isConnectingToInternet()){
			
			T.ShortToast(R.string.download_fail);
			sendBroadcast(0, id);
			stopTask(id);
			return;
		}

		new Thread()
		{
			public void run()
			{
				long totalLength = 0;
				if (downloadSize.get(id) == null)
				{
					totalLength = DownLoadUtil.getFileLength(url);
					int retry = 25;
					int i = 0;
					while (totalLength == -1)
					{
						totalLength = DownLoadUtil.getFileLength(url);
						if (i++ > retry) break;
					}
					if (totalLength != -1) downloadSize.put(id, totalLength);
					else
					{
						L.i("获取不到  total length return ");
						stopTask(id);
						sendBroadcast(0, id);
						return;
					}

				}
				else totalLength = downloadSize.get(id);

				final long total = totalLength;

				HttpHandler<File> hh = new AbaseHttp().download(url, downloadPath, true, totalLength, new AjaxCallBack<File>()
				{
					@Override
					public void onStart()
					{
						L.i(name + " start download ");
						downloadingSet.add(id);
					}
					
					@Override
					public void onFailure(Throwable t, int errorNo, String strMsg)
					{
						L.i("download fail  error no =" + errorNo + "msg = " + strMsg + " exception =" + t.getClass());
						if (errorNo == 403)
						{
							stopTask(id);
							T.ShortToast(R.string.download_not_found_app);
						}
						else if (errorNo == 416)
						{
							deleteTask(id);
							SysIntentUtil.install(DownloadService.this, downloadPath);
							T.ShortToast(R.string.download_app_exist);
						}
						else if (strMsg!=null&& strMsg.equals("user stop download thread"))
						{
							stopTask(id);
							
							if (infos.get(id) != null)
							{
								Intent intent2 = new Intent("com.ustore.download.notication");
								intent2.putExtra("id", id);
								intent2.putExtra("name", name);
								intent2.putExtra("url", url);
								intent2.putExtra("imgurl", imgurl);
								intent2.putExtra("pause", true);
								sendBroadcast(intent2);
								return;
							}
						}
						else if (NetworkUtil.isConnectingToInternet())
						{
							stopTaskNotStopNotifiction(id);

							L.i(" 开始 重试 下载");
							Intent intent2 = new Intent(DownloadService.this, DownloadService.class);
							intent2.putExtra("id", id);
							intent2.putExtra("name", name);
							intent2.putExtra("url", url);
							intent2.putExtra("imgurl", imgurl);
							startService(intent2);
							
							return;
						}
						else stopTask(id);

						sendBroadcast(0, id);

						finish();
					}

					@Override
					public void onLoading(long count, long current)
					{

						String p = (current * 100 / total) + "";
						final String process = p;
						dao.updateDownloadListInfos(id, p);
						Notification notion = notifcaitions.get(id);

						if (notion == null)
						{
							new Thread()
							{
								public void run()
								{
									showUpdateNotify(id, name, url, imgurl, process);
								};
							}.start();
						}
						else
						{
							RemoteViews contentView = notion.contentView;
							contentView.setProgressBar(R.id.pb_notify, 100, Integer.parseInt(process), false);
							contentView.setTextViewText(R.id.notity_percent, process + "%");
							if (downloadingSet.contains(id))
							{
								contentView.setImageViewResource(R.id.iv_bt, R.drawable.pause);
								contentView.setTextViewText(R.id.tv_bt_text, getText(R.string.pause));
							}
							ManageUtil.getNotificationManager().notify(Integer.parseInt(id), notion);

						}

					}

					@Override
					public void onSuccess(File t)
					{

						if (t.length() < total)
						{
							L.i("download return  and as total");
							stopTask(id);
							sendBroadcast(0, id);
							return;
						}
						deleteTask(id);
						T.ShortToast(name + getText(R.string.download_success));
						
						Info info = infos.get(id);
						// if auto install model
						if (info != null && info.install == 0)
						{
							L.i("start auto  install ");
							ManageUtil.getNotificationManager().cancel(Integer.parseInt(id));
							if (Config.ROOT) RootUtil.install(DownloadService.this, t.getPath(), null);
							else SysIntentUtil.install(DownloadService.this, t);

						}
						else
						{
							L.i("show install notification");
							
							new Thread()
							{
								public void run()
								{
									showInstallNotifi(id, name, imgurl);
								};
							}.start();
						}


						if (TelUtil.getSubscriberId() != null)
						{
							// 统计 下载次数
							new AbaseHttp().get(Website.DOWNLOAD_COUNT + id, null);
							L.i("send  download statiscs url=" + Website.DOWNLOAD_COUNT + id);
						}

						// if push info 如果是推送 数据 信息 返回一个 统计数据
						if (info != null)
						{
							PushEngine.download(info, downloadPath);
							infos.remove(id);
						}

						sendBroadcast(1, id);
						DownloadService.this.sendBroadcast(new Intent("broadcast.adminupdate"));

						finish();
					}

				});
				handlers.put(id, hh);
			}
		}.start();

	}

	/***
	 * 完成以后 结束 服务
	 */
	private void finish()
	{
		if (downloadSet.size() == 0)
		{
			unregisterReceiver(receiver);

			this.stopSelf();
		}

	}

	private void showUpdateNotify(String myid, String name, String url, String imgurl, String process)
	{
		final int id = Integer.parseInt(myid);
		Info info = infos.get(myid);

		final NotificationManager manager = ManageUtil.getNotificationManager();
		Notification notification = new Notification(R.drawable.icon, R.string.download_loading+ name, System.currentTimeMillis());

		notification.defaults |= Notification.DEFAULT_LIGHTS;

		if (info == null) notification.flags |= Notification.FLAG_AUTO_CANCEL;
		else if (info != null && info.clear == 0) notification.flags |= Notification.FLAG_NO_CLEAR;

		RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification_download);

		Bitmap bm = DownLoadUtil.getBitmap(imgurl);

		if (bm == null) bm = drawableToBitmap(this.getResources().getDrawable(R.drawable.icon));

		contentView.setImageViewBitmap(R.id.iv_notify, bm);
		contentView.setTextViewText(R.id.tv_notify, name);
		contentView.setProgressBar(R.id.pb_notify, 100, Integer.parseInt(process), false);
		contentView.setTextViewText(R.id.notity_percent, process + "%");

		// IV Button
		Intent intent2 = new Intent("com.ustore.download.notication");
		intent2.putExtra("id", myid);
		intent2.putExtra("name", name);
		intent2.putExtra("url", url);
		intent2.putExtra("imgurl", imgurl);

		contentView.setOnClickPendingIntent(R.id.iv_bt, PendingIntent.getBroadcast(this, id, intent2, PendingIntent.FLAG_UPDATE_CURRENT));

		notification.contentView = contentView;

		if (info != null && info.reveal == 1) notification.contentIntent = null;
		else
		{
			Intent intent = new Intent(this.getApplicationContext(), DownDetails.class);
			intent.putExtra("id", myid);
			intent.putExtra("url", url);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pi = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.contentIntent = pi;
		}

		if (downloadingSet.contains(myid)) manager.notify(id, notification);

		notifcaitions.put(myid, notification);

	}

	/**
	 * 显示通知 单击 安装 应用
	 * 
	 * @param myid
	 * @param
	 */
	private void showInstallNotifi(String myid, String name, String icon)
	{

		Intent i = install(myid);
		final int id = Integer.parseInt(myid);

		if (i != null)
		{
			PendingIntent pi = PendingIntent.getActivity(this, id, i, 0);

			final NotificationManager manager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

			Notification notification = new Notification(R.drawable.icon, getText(R.string.download_success), System.currentTimeMillis());

			notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification);

			Bitmap bm = DownLoadUtil.getBitmap(icon);
			if (bm == null) bm = drawableToBitmap(this.getResources().getDrawable(R.drawable.icon));

			contentView.setImageViewBitmap(R.id.i1, bm);
			contentView.setTextViewText(R.id.tv1, name + getText(R.string.download_success));
			contentView.setTextViewText(R.id.tv2, getText(R.string.click_install));
			notification.contentView = contentView;
			notification.contentIntent = pi;
			manager.notify(id, notification);

			new Timer().schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					manager.cancel(id);
				}
			}, 20000);

		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		if (drawable != null) return ((BitmapDrawable) drawable).getBitmap();
		else return null;
	}

	/**
	 * 安装APK文件
	 */
	public Intent install(String myID)
	{
		StringBuffer apkname = new StringBuffer(myID);
		apkname.append(".apk"); // 得到app的名称
		StringBuffer mSavePath = new StringBuffer(Website.sd_path);
		mSavePath.append("download");
		File apkfile = new File(mSavePath.toString(), apkname.toString());
		if (!apkfile.exists()) { return null; }

		Intent i = null;
		// 通过Intent安装APK文件
		try
		{
			i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return i;
	}

	public static Intent getSendIntent(Context context, String appid, String name, String icon, String url, boolean root)
	{
		Intent intent = new Intent(context, DownloadService.class);

		intent.putExtra("id", appid);
		intent.putExtra("name", name);
		intent.putExtra("imgurl", icon);
		intent.putExtra("url", url);
		intent.putExtra("root", root);

		return intent;
	}

	/**
	 * delete task
	 * 
	 * @param id
	 */
	public static void deleteTask(String id)
	{
		stopTask(id);
		dao.delete(id);
		dao.deleteDownloadList(id);

	}

	/**
	 * stop task
	 * 
	 * @param id
	 */
	public static void stopTask(String id)
	{
		downloadingSet.remove(id);
		downloadSet.remove(id);

		if (handlers.get(id) != null)
		{
			handlers.get(id).stop();
			handlers.remove(id);
		}

		ManageUtil.getNotificationManager().cancel(Integer.parseInt(id));

		notifcaitions.remove(id);

	}

	public static void stopTaskNotStopNotifiction(String id)
	{
		downloadingSet.remove(id);
		downloadSet.remove(id);

		if (handlers.get(id) != null)
		{
			handlers.get(id).stop();
			handlers.remove(id);
		}

	}

	/**
	 * remove task
	 * 
	 * @param id
	 */
	public static void removeTaskAndDeleteFile(String id)
	{
		deleteTask(id);
		File f = new File(Website.sd_path + "download/" + id + ".apk");
		if (f.exists()) f.delete();

	}


}
