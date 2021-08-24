package com.jayqqaa12.abase.util;

import android.content.pm.PackageManager.NameNotFoundException;

import com.jayqqaa12.abase.core.Abase;

/***
 * 用来 判断 版本
 * 
 * @author 12
 * 
 */
public class VersionUtil 
{

	/***
	 * 获得 版本号
	 * 
	 * @return
	 */
	public static String getVersionName()
	{
		try
		{
			return ManageUtil.getPackManager().getPackageInfo(Abase.getContext().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return "0";

	}

	public static int getVersionCode()
	{
		try
		{
			return ManageUtil.getPackManager().getPackageInfo(Abase.getContext().getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return 0;

	}
	

	/***
	 * 
	 * @param serverVersion
	 * @return
	 */
	public static boolean isNewVersion(int serverVersion)
	{
		return serverVersion >VersionUtil.getVersionCode();
	}

}
