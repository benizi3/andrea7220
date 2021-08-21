package com.ustore.engine;

import java.io.File;

import net.tsz.afinal.http.AjaxCallBack;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.Abase;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.expand.BaiduLocation;
import com.jayqqaa12.abase.util.ConfigUtil;
import com.jayqqaa12.abase.util.VersionUtil;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.Provider;
import com.jayqqaa12.abase.util.common.Validate;
import com.jayqqaa12.abase.util.io.FileUtil;
import com.jayqqaa12.abase.util.sys.RootUtil;
import com.ustore.bean.HomePage;
import com.ustore.bean.Version;
import com.ustore.http.Config;
import com.ustore.http.Website;

public class InitEngine
{

	private static int i = 0;

	public void init(Context context)
	{
		Abase.setContext(context);
		initFile();
		initLocation();

		getHomePage(context);
		PushEngine.getPushTimeoutSetting();
		PushEngine.registUser();
		PushEngine.pushInstall(context);

	}

	
	/***
	 * set custom id
	 * 
	 * @param custom
	 */
	public static void setCustom(String custom)
	{
		Website.CUSTOM_ID = custom;
	}

	private void initLocation()
	{

		BaiduLocation.initLocationClient(new BDLocationListener()
		{
			@Override
			public void onReceivePoi(BDLocation arg0)
			{}

			@Override
			public void onReceiveLocation(BDLocation location)
			{
				L.i("update location = " + location.getLatitude() + "," + location.getLongitude() + " error code ="
						+ location.TypeServerError);
				String province = location.getProvince();
				String city = location.getCity();
				String area = location.getDistrict();

				if (i++ > 10)
				{
					L.i("so long get fail stop location");
					BaiduLocation.stop();
				}

				if (province != null && city != null)
				{
					saveOrUpdateLocation(province, city, area);
					BaiduLocation.stop();
					PushEngine.registUser();
				}
			}
		});
	}

	/**
	 * init file and other
	 */
	private void initFile()
	{
		L.i("init file");

		if (ACache.isInit(Config.IS_INIT))
		{
			L.i("init  delete download file");
			FileUtil.deleteDirectory(Website.sd_path + "download");
		}
		File f = new File(Website.sd_path + "download");
		if (!f.exists()) f.mkdirs();

	}

	/***
	 * 从 服务器 获得 设置的主页 信息
	 */
	private void getHomePage(final Context context)
	{
		if (ACache.isPastDue(Config.HOME_PAGE, ACache.TIME_DAY) || ConfigUtil.getString(Config.HOME_PAGE, null) == null)
		{
			if (ConfigUtil.getBoolean(Config.HOME_PAGE_RUNNING, false)) return;
			ConfigUtil.setValue(Config.HOME_PAGE_RUNNING, true);

			new AbaseHttp().get(Website.GET_HOME_PAGE, new AjaxCallBack<String>()
			{
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{
					ConfigUtil.setValue(Config.HOME_PAGE_RUNNING, false);
				}

				@Override
				public void onSuccess(String result)
				{
					L.i("save home page =" + result);
					if (Validate.notEmpty(result) && !result.contains("<body>"))
					{
						try
						{
							boolean status = true;
							HomePage homepage = new Gson().fromJson(result, HomePage.class);
							if ("1".equals(homepage.status)) status = true;
							else status = false;
							ConfigUtil.setValue(Config.HOME_PAGE, homepage.url);
							ConfigUtil.setValue(Config.HOME_PAGE_OPEN, status);

							if (status)
							{
								Uri uri = Uri.parse(Config.HOME_PAGE_PROVIDEE_AUTHORITIES + "/" + Provider.UPDATE);
								ContentValues values = new ContentValues();
								values.put("homepage", homepage.url);
								context.getContentResolver().update(uri, values, null, null);
							}

						} catch (Exception e)
						{
							e.printStackTrace();
						} finally
						{
							ConfigUtil.setValue(Config.HOME_PAGE_RUNNING, false);
						}
					}
				}
			});
		}

	}

	private void saveOrUpdateLocation(String province, String city, String area)
	{
		String p = ConfigUtil.getString("province", null);
		String c = ConfigUtil.getString("city", null);
		String a = ConfigUtil.getString("area", null);

		if (p == null || !Validate.equals(p, province))
		{
			ConfigUtil.setValue("province", province);
			ConfigUtil.setValue("LOCATION_CHANGE", true);
		}
		if (c == null || !Validate.equals(c, city))
		{
			ConfigUtil.setValue("city", city);
			ConfigUtil.setValue("LOCATION_CHANGE", true);
		}
		if (a == null || !Validate.equals(a, area))
		{
			ConfigUtil.setValue("area", area);
			ConfigUtil.setValue("LOCATION_CHANGE", true);
		}

	}

}
