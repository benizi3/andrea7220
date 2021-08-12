package com.ustore.activity;

import java.io.File;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;

import com.google.gson.Gson;
import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.util.ConfigUtil;
import com.jayqqaa12.abase.util.VersionUtil;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.T;
import com.jayqqaa12.abase.util.network.ApnUtil;
import com.jayqqaa12.abase.util.network.DownLoadUtil;
import com.jayqqaa12.abase.util.network.NetworkUtil;
import com.jayqqaa12.abase.util.phone.TelUtil;
import com.jayqqaa12.abase.util.sys.SysIntentUtil;
import com.jayqqaa12.abase.util.ui.DialogUtil;
import com.ustore.bean.Version;
import com.ustore.engine.PushEngine;
import com.ustore.http.Config;
import com.ustore.http.HttpGetRes;
import com.ustore.http.Website;

public class Main extends Activity
{

	boolean update = false;
	ProgressDialog pd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setApn();
		PushEngine.getKeyword();
		PushEngine.liveness();
		prepare();

		boolean root = Config.ROOT;

	}

	/**
	 * 跳转到首页
	 */
	private void prepare()
	{
		
		if(!NetworkUtil.isConnectingToInternet())
		{
			DialogUtil.showNotNetworkDialog(this);
			
			return ;
		}
		
			new Thread()
			{
				public void run()
				{
					boolean isUpdate = ACache.isPastDue("version_update_check", 3 * ACache.TIME_DAY);

					L.i("now must check update  is =" + isUpdate);

					if (true)
					{
						Looper.prepare();
						checkVersion();
						Looper.loop();
					}
					else
					{
						try
						{
							Thread.sleep(555);
							intoMain();
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				};
			}.start();
	}

	private void checkVersion()
	{
		/**
		 * 对比服务器 版本 检查更新
		 */
		final String path = Website.sd_path + "download/ustore.apk";

		try
		{
			String result = DownLoadUtil.downloadString((Website.APK_UPDATE));
			L.i("check update version " + result);

			Version v = new Gson().fromJson(result, Version.class);

			update = VersionUtil.isNewVersion(v.version_code);

			int diff = v.version_code - VersionUtil.getVersionCode();

			if (diff >= 2 && ACache.isPastDue("version_update_check", 30 * ACache.TIME_DAY))
			{

				// 重新提示更新
				ConfigUtil.setValue(Config.VERSION_DIALOG, true);
				ConfigUtil.setValue(Config.VERSION_CHECK_COUNT, 0);

				L.i("retry update");
			}

		} catch (Exception e)
		{
			update = false;
		}
		if (update && ConfigUtil.getBoolean(Config.VERSION_DIALOG, true))
		{

			Dialog dialog = new AlertDialog.Builder(this).setMessage("发现新版本 是否更新")// 设置内容
					.setPositiveButton("取消",// 确定按钮
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which)
								{
									ConfigUtil.setValue(Config.VERSION_CHECK_COUNT,
											ConfigUtil.getInteger(Config.VERSION_CHECK_COUNT, 0) + 1);

									if (ConfigUtil.getInteger(Config.VERSION_CHECK_COUNT, 0) >= 3)
									{
										ConfigUtil.setValue(Config.VERSION_DIALOG, false);
									}
									dialog.dismiss();
									intoMain();
								}
							}).setNegativeButton("立即更新", // 設置按钮
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int whichButton)
								{
									new AbaseHttp().download(Website.APK_UPDATE_DOWNLOAD, path, true, new AjaxCallBack<File>()
									{
										@Override
										public void onFailure(Throwable t, int errorNo, String strMsg)
										{

											pd.dismiss();
											T.ShortToast("更新失败 ");
											intoMain();
										}

										@Override
										public void onStart()
										{
											pd = DialogUtil.createProgressDialog(Main.this, "正在下载更新。。", false,
													ProgressDialog.STYLE_HORIZONTAL);
											pd.setButton("取消更新", new OnClickListener()
											{
												@Override
												public void onClick(DialogInterface dialog, int which)
												{
													pd.dismiss();
													dialog.dismiss();
													intoMain();
												}
											});
											pd.show();
										}

										@Override
										public void onLoading(long count, long current)
										{
											pd.setProgress((int) (current * 100 / count));
											L.i("download updae process =" + current * 100 / count);
										}

										@Override
										public void onSuccess(File t)
										{
											pd.dismiss();
											intoMain();
											SysIntentUtil.install(Main.this, t);

										}

									});

								}
							}).create();
			dialog.setCancelable(false);
			dialog.show();
		}
		else
		{
			try
			{
				Thread.sleep(555);
				intoMain();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 因为 wap 访问不了 www
	 * 
	 */
	private void setApn()
	{
		
		if (NetworkUtil.isGPRS())
		{
			try
			{
				L.i(" now apn is =" + ApnUtil.isWapApn());
				if (ApnUtil.isWapApn()) DialogUtil.showChangeApnDialog(this);
			} catch (Exception e)
			{}
		}
	}

	private void intoMain()
	{
		Intent intent = new Intent(Main.this, Home.class);
		startActivity(intent);
		Main.this.finish();

		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

}
