package com.jayqqaa12.abase.util;

import android.content.pm.PackageManager.NameNotFoundException;

import com.jayqqaa12.abase.core.AbaseUtil;
import com.jayqqaa12.abase.util.network.DownLoadUtil;
import com.jayqqaa12.abase.util.security.Validate;

/***
 * 用来 判断 版本
 * 
 * @author 12
 * 
 */
public class VersionUtil extends AbaseUtil
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
			return ManageUtil.getPackManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return "0";

	}
	
	
	/***
	 * 
	 * @param serverVersion
	 * @return
	 */
	public  static boolean  isNewVersion(String serverVersion)
	{
		return !Validate.equals(serverVersion, VersionUtil.getVersionName());
	}
	
	
	/***
	 * 传入 服务器 版本 与本地 版本 对比 
	 * @param serverVersion
	 * @return
	 */
	public static boolean haveNewVersion(String url){
		
		return !Validate.equals(DownLoadUtil.downloadString(url), getVersionName());
	}
	
	

	/***
	 * 输入 服务器 地址 获得 新版本 信息
	 * 
	 * @param url
	 * @return
	 */
	public static String getNewVersionInfo(String url)
	{
		return DownLoadUtil.downloadString(url);
	}
	
	
	
	
	
	
	
	
	
	
	

}
