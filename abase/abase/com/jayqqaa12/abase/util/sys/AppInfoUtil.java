package com.jayqqaa12.abase.util.sys;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

import com.jayqqaa12.abase.core.AbaseUtil;
import com.jayqqaa12.abase.util.ManageUtil;

/**
 * apk 的 相关 信息
 * 
 * @author 12
 * 
 */
public class AppInfoUtil extends AbaseUtil
{

	/**
	 * 获得 版本号
	 * 
	 * @return
	 */
	public static String getVersionNo()
	{
		try
		{

			PackageInfo info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
			return info.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return "0";
		}

	}

	/**
	 * 通过包名获取应用程序的名称。
	 * 
	 * 加载 程序 做不到 异步！！
	 * 
	 * @param context
	 *            Context对象。
	 * @param packageName
	 *            包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(String packageName)
	{
		PackageManager pm = getContext().getPackageManager();
		String name = null;
		try
		{
			name = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取有关本程序的packname。
	 * 
	 * @return 有关本程序的信息。
	 */
	public static String getMyApkPackname()
	{
		ApplicationInfo applicationInfo = getContext().getApplicationInfo();
		return applicationInfo.packageName;

	}

	/**
	 * 
	 * 获得 有桌面 图标的 应用
	 * 
	 * @return
	 */
	public static List<AppInfo> getLauncherApp()
	{
		List<AppInfo> apks = new ArrayList<AppInfo>();

		PackageManager pm = ManageUtil.getPackManager();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resovleInfos = pm.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

		for (ResolveInfo info : resovleInfos)
		{
			try
			{
				AppInfo apk = getAppInfo(pm, pm.getPackageInfo(info.activityInfo.packageName, 0));
				apk.appName = info.loadLabel(pm).toString();
				apk.iconDrawable = info.loadIcon(pm);
				apk.packageName = info.activityInfo.packageName;

				apks.add(apk);
			}
			catch (NameNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return apks;

	}

	/**
	 * 获取有关本程序的信息。
	 * 
	 * @return 有关本程序的信息。
	 */
	public static AppInfo getMyApkInfo()
	{
		AppInfo apkInfo = new AppInfo();
		Context context = getContext();

		ApplicationInfo applicationInfo = context.getApplicationInfo();
		apkInfo.packageName = applicationInfo.packageName;

		apkInfo.iconId = applicationInfo.icon;
		Resources mResources = context.getResources();
		apkInfo.iconDrawable = mResources.getDrawable(apkInfo.iconId);
		apkInfo.appName = mResources.getText(applicationInfo.labelRes).toString();
		apkInfo.appSize = new File(applicationInfo.publicSourceDir).length();
		apkInfo.size = Formatter.formatFileSize(getContext(), apkInfo.appSize);
		apkInfo.date = new Date(new File(applicationInfo.sourceDir).lastModified());

		PackageInfo packageInfo = null;
		try
		{
			packageInfo = context.getPackageManager().getPackageInfo(apkInfo.packageName, 0);
			apkInfo.versionCode = packageInfo.versionCode;
			apkInfo.versionName = packageInfo.versionName;

		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return apkInfo;
	}

	/**
	 * 获取有关本程序的信息。
	 * 
	 * @return 有关本程序的信息。
	 */
	public static String getMyApkName()
	{
		Context context = getContext();
		ApplicationInfo applicationInfo = context.getApplicationInfo();
		return context.getResources().getText(applicationInfo.labelRes).toString();

	}

	/**
	 * 返回当前手机里面安装的所有的程序信息的集合
	 * 
	 * @return 应用程序的集合
	 */
	public static List<AppInfo> getAllApps()
	{
		List<AppInfo> list = new ArrayList<AppInfo>();
		PackageManager packmanager = ManageUtil.getPackManager();

		List<PackageInfo> packinfos = packmanager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo info : packinfos)
		{
			AppInfo apkinfo = getAppInfo(packmanager, info);

			list.add(apkinfo);
		}
		return list;
	}

	/**
	 * 根据 路径 返回 apk 信息
	 * 
	 * @param apkpath
	 * @return
	 */
	public static AppInfo getApkInfo(String apkpath)
	{
		AppInfo apk = null;
		PackageManager pm = getContext().getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkpath, PackageManager.GET_ACTIVITIES);
		if (info != null) apk = getAppInfo(pm, info);

		return apk;
	}

	private static AppInfo getAppInfo(PackageManager packmanager, PackageInfo packageInfo)
	{
		AppInfo info = new AppInfo();
		info.versionCode = packageInfo.versionCode;
		info.versionName = packageInfo.versionName;
		String packname = packageInfo.packageName;
		info.packageName = packname;
		ApplicationInfo applicationInfo = packageInfo.applicationInfo;
		Drawable icon = applicationInfo.loadIcon(packmanager);
		info.iconId = applicationInfo.icon;
		info.iconDrawable = icon;
		info.uid = applicationInfo.uid;
		if (applicationInfo.publicSourceDir != null)
		{
			info.appSize = new File(applicationInfo.publicSourceDir).length();
			info.size = Formatter.formatFileSize(getContext(), info.appSize);
		}
		String appname = applicationInfo.loadLabel(packmanager).toString();
		info.appName = appname;

		if (!filterApp(applicationInfo))
		{
			info.isSysApp = true;

		}
		else if (applicationInfo.publicSourceDir != null)
		{
			// 系统应用 不知道时间
			info.date = new Date(new File(applicationInfo.sourceDir).lastModified());
		}
		
		// TODO 判断 ROOT 进来的 SYSTEM app 目前的办法是 需要 有一个 常用 第三方 应用的 名单 然后 排除掉
		// 但是应该有更好的办法

		return info;
	}

	/**
	 * 返回 我安装的 可以卸载的软件
	 * 
	 * @return 应用程序的集合
	 */
	public static List<AppInfo> getMyApps()
	{
		return getMyApps(getAllApps());
	}

	/**
	 * 如题
	 * 
	 * @return
	 */
	public static List<AppInfo> getNotSysApps()
	{
		return getNotSysApps(getAllApps());
	}

	/**
	 * 如题
	 * 
	 * @return
	 */
	public static List<AppInfo> getNotSysApps(List<AppInfo> apks)
	{

		List<AppInfo> list = new ArrayList<AppInfo>();

		for (AppInfo info : apks)
		{
			if (!info.isSysApp)
			{
				list.add(info);
			}
		}
		return list;

	}

	/**
	 * 返回 我安装的 可以卸载的软件
	 * 
	 * @return 应用程序的集合
	 */
	public static List<AppInfo> getMyApps(List<AppInfo> apps)
	{

		List<AppInfo> list = new ArrayList<AppInfo>();

		for (AppInfo info : apps)
		{
			if (!info.isSysApp && !info.isPreloadApp)
			{
				list.add(info);
			}
		}
		return list;
	}

	/**
	 * 返回 系统 App
	 * 
	 * @return 应用程序的集合
	 */
	public static List<AppInfo> getSysApps()
	{
		return getSysApps(getAllApps());
	}

	/**
	 * 
	 * 返回 pre load App
	 * 
	 * @return
	 */

	public static List<AppInfo> getPreloadApps()
	{

		return getPreloadApps(getAllApps());
	}

	/**
	 * 返回 系统 App
	 * 
	 * @return 应用程序的集合
	 */
	public static List<AppInfo> getSysApps(List<AppInfo> apps)
	{
		List<AppInfo> list = new ArrayList<AppInfo>();

		for (AppInfo info : apps)
		{
			if (info.isSysApp)
			{
				list.add(info);
			}
		}
		return list;
	}

	public static List<AppInfo> getPreloadApps(List<AppInfo> apps)
	{
		List<AppInfo> list = new ArrayList<AppInfo>();

		for (AppInfo info : apps)
		{
			if (info.isPreloadApp)
			{
				list.add(info);
			}
		}
		return list;
	}

	/**
	 * 判断某个应用程序是 不是三方的应用程序
	 * 
	 * @param info
	 * @return
	 */
	public static boolean filterApp(ApplicationInfo info)
	{

		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) return true;
		else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { return true; }
		return false;
	}

}
