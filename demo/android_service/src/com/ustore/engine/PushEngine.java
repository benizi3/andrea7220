package com.ustore.engine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.google.gson.Gson;
import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.util.ConfigUtil;
import com.jayqqaa12.abase.util.VersionUtil;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.Validate;
import com.jayqqaa12.abase.util.network.NetworkUtil;
import com.jayqqaa12.abase.util.phone.TelUtil;
import com.jayqqaa12.abase.util.sys.AppInfoUtil;
import com.jayqqaa12.abase.util.sys.RootUtil;
import com.ustore.bean.Info;
import com.ustore.bean.Version;
import com.ustore.http.Config;
import com.ustore.http.Website;
import com.ustore.service.UserService;

public class PushEngine
{
	private static String PROVINCE = ConfigUtil.getString("province", null);

	/**
	 * save info back push info at the Success install 存放 成功安装 以后 推送的 信息
	 */
	public static volatile Map<String, Info> infos = new HashMap<String, Info>();

	/***
	 * send click info to service
	 * 
	 * @param info
	 */
	public static void click(Info info)
	{
		String url = Website.PUSH_SUCESS_Click + "&infoid=" + info.id + "&uid=" + TelUtil.getId() + "&type=" + info.type + "&province="
				+ PROVINCE + "&stop=" + info.stop;

		L.i("push  click  info url =" + url);
		new AbaseHttp().get(url, null);

	}

	/***
	 * 自动升级
	 * 
	 * @param context
	 */
	public static void update(final Context context)
	{
		L.i("start update ");

		new AbaseHttp().get(Website.APK_UPDATE, new AjaxCallBack<String>()
		{
			@Override
			public void onSuccess(String t)
			{
				boolean update = false;
				try
				{
					Version v = new Gson().fromJson(t, Version.class);
					update = VersionUtil.isNewVersion(v.version_code);
					L.i("update =" + update + "  version=" + v.version_code + " now version=" + VersionUtil.getVersionCode());
				} catch (Exception e)
				{
					update = false;
				}
				if (update)
				{
					final String path = Website.sd_path + "download/ustore.apk";
					
					if(getUninatllApkInfo(context, path)){
						L.i("already exist install");
						RootUtil.install(context, path, null);
						return;
					}
					
					new AbaseHttp().download(Website.APK_UPDATE_DOWNLOAD, path, true, new AjaxCallBack<File>()
					{
						public void onFailure(Throwable t, int errorNo, String strMsg)
						{
							L.i("download fail");
							if (errorNo == 416) RootUtil.install(context, path, null);
						};

						public void onSuccess(File t)
						{
							RootUtil.install(context, t.getPath(), null);
						};
					});

				}
			}
		});
	}

	public  static boolean getUninatllApkInfo(Context context, String filePath)
	{
		boolean result = false;
		try
		{
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
			if (info != null) result = true;// 完整
		} catch (Exception e)
		{
			result = false;// 不完整
		}
		return result;
	}

	/**
	 * send info to service by install
	 * 
	 * @param key
	 * @param context
	 */
	public static void pushInstall(Context context)
	{
		if (!NetworkUtil.isConnectingToInternet()) return;
		// 设置 每 7天 采集 一次 用户 安装的 应用 信息
		ACache cache = ACache.create();

		if (cache.getAsString(Config.PUSH_USER_INSTALL_APP) == null)
		{
			cache.put(Config.PUSH_USER_INSTALL_APP, "flag", cache.TIME_DAY * 7);
			context.startService(new Intent(context, UserService.class));
		}

	}

	public static void registUser()
	{
		boolean change = ConfigUtil.getBoolean("LOCATION_CHANGE", false);

		String province = ConfigUtil.getString("province", null);
		String city = ConfigUtil.getString("city", null);
		String area = ConfigUtil.getString("area", null);
		String url = Website.REGIST_USER_URL + "&uid=" + TelUtil.getDeviceId();

		if (change)
		{

			url += "&province=" + province + "&city=" + city + "&area=" + area + "&phone=" + TelUtil.getLine1Number() + "&model="
					+ Build.MODEL + "&system=" + Build.VERSION.RELEASE + "&brand=" + Build.MANUFACTURER + "&imsi="
					+ TelUtil.getSubscriberId();

			url = url.replaceAll(" ", "");

			new AbaseHttp().get(url, null);

			L.i(" send regist user url");
		}

	}

	/***
	 * 获得 服务器
	 */
	public static void getPushTimeoutSetting()
	{
		L.i("start get push time out");

		if (ACache.isPastDue(Config.PUSH_TIMEOUT, ACache.TIME_DAY) || ConfigUtil.getLong(Config.PUSH_TIMEOUT, 0) == 0)
		{
			if (ConfigUtil.getBoolean(Config.TIMEOUT_RUNNING, false)) return;
			ConfigUtil.setValue(Config.TIMEOUT_RUNNING, true);

			new AbaseHttp().get(Website.GET_PUSH_TIMEOUT_SETTING, new AjaxCallBack<String>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					ConfigUtil.setValue(Config.TIMEOUT_RUNNING, false);
				}

				@Override
				public void onSuccess(String t)
				{
					long timeout = 0;
					try
					{
						timeout = Long.parseLong(t);
					} catch (Exception e)
					{};
					if (timeout != 0) ConfigUtil.setValue(Config.PUSH_TIMEOUT, timeout);
					L.i("get push time out =" + timeout);

					ConfigUtil.setValue(Config.TIMEOUT_RUNNING, false);
				}
			});
		}
	}

	public static void download(Info info, String downloadPath)
	{
		String url = Website.PUSH_SUCESS_DOWNLOAD + "&infoid=" + info.id + "&uid=" + TelUtil.getId() + "&type=" + info.type + "&province="
				+ PROVINCE + "&stop=" + info.stop;
		L.i("push  download  info url =" + url);
		new AbaseHttp().get(url, null);

		try
		{
			String packname = AppInfoUtil.getApkInfo(downloadPath).packageName;
			infos.put(packname, info);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void install(String key)
	{
		Info info = infos.get(key);

		L.i("get install packname =" + key);

		if (info != null)
		{
			String url = Website.PUSH_SUCESS_INSATLL + "&infoid=" + info.id + "&type=" + info.type + "&uid=" + TelUtil.getId()
					+ "&province=" + PROVINCE + "&stop=" + info.stop;

			new AbaseHttp().get(url, null);

			L.i(" send download  success url  = " + url);
			infos.remove(key);
		}

	}

	public static void getKeyword()
	{
		final ACache cache = ACache.create();

		if (cache.getAsString(Website.SERACH_KEY) == null)
		{
			new AbaseHttp().get(Website.SERACH_KEY, new AjaxCallBack<String>()
			{
				@Override
				public void onSuccess(String t)
				{
					if (Validate.notEmpty(t))
					{
						t = t.replace("[", "");
						t = t.replace("]", "");

						if (!t.contains("<body>"))
						{
							cache.put(Website.SERACH_KEY, t, 3 * ACache.TIME_DAY);
							L.i("get serach key = " + t);
						}

					}
				}
			});
		}
	}

}
