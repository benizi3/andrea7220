package com.ustore.service;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.core.service.AbaseService;
import com.jayqqaa12.abase.model.AppInfo;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.sys.AppInfoUtil;
import com.ustore.http.Website;

/**
 * 采集 用户 信息
 * 
 * @author 12
 * 
 */
public class UserService extends AbaseService
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		new Thread()
		{
			public void run()
			{
				Looper.prepare();
				uploadAppInfo();
				Looper.loop();
			}
		}.start();

	}

	private void uploadAppInfo()
	{
		L.i("start up app info  service ");

		new AbaseHttp().get(Website.GET_USER_INSTALL_APPS_LIST, new AjaxCallBack<String>()
		{

			@Override
			public void onSuccess(String t)
			{
				List<String> packs = new ArrayList<String>();
				try
				{
					if (!t.startsWith("[") || !t.endsWith("]"))
					{
						L.i(" info is error  and info =" + t);
						return;
					}

					packs = new Gson().fromJson(t, new TypeToken<List<String>>()
					{}.getType());
				} catch (Exception e)
				{
					packs = new ArrayList<String>();
				}

				L.i(" get update packname =" + packs);

				List<AppInfo> list = new ArrayList<AppInfo>();

				for (AppInfo app : AppInfoUtil.getAllApps())
				{
					app.date = null;
					app.iconDrawable = null;

					if (!app.isSysApp) list.add(app);
					else if (packs != null && packs.contains(app.packageName)) list.add(app);

				}

				AjaxParams params = new AjaxParams();
				params.put("apps", new Gson().toJson(list));
				new AbaseHttp().post(Website.PUSH_USER_INSTALL_APPS, params, new AjaxCallBack<Object>()
				{
					@Override
					public void onSuccess(Object t)
					{
						UserService.this.stopSelf();
					}
				});
			}

		});

	}

}
