package com.ustore.http;

import java.io.IOException;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.network.DownLoadUtil;
import com.jayqqaa12.abase.util.sys.SysIntentUtil;

public class HttpGetRes
{
	private String httpUrl = null;
	private String resultinfo = null;
	private String readname = null;
	private Activity context = null;
	private int retry = 15;
	private int count = 0;
	public boolean showDialog = true;

	private ACache cache = ACache.create();

	public HttpGetRes(Activity context)
	{
		this.context = context;
	}

	/** 网络连接的地址 **/
	public void seturl(String strurl)
	{
		this.httpUrl = strurl;

	}

	/** 网络连接获取的数据字符串 **/
	public String getResult()
	{
		return this.resultinfo;

	}

	/** 写入到sd卡文件名 **/
	public void set_fileName(String name)
	{
		this.readname = name;
	}

	/**
	 * 连接网络获取数据,同时将数据写入缓存
	 * 
	 * @param detail
	 */
	public void show(final int cacheTime)
	{

		String result = null;
		try
		{
			result = DownLoadUtil.downloadString(httpUrl);

			if (result == null) return;

			if (result.split("\\}\\{").length == 1 && !result.endsWith("}") && result.startsWith("{"))
			{
				L.i("获取 数据异常  data=" + result);
				showDialog("当前网络无法连接互联网 请重新设置");
			}
			else
			{
				this.resultinfo = result;
				// 写入 缓存
				cache.put(httpUrl, result, cacheTime);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			L.i("获得 列表 出现 异常  data=" + result);
			this.resultinfo = null;

			if (retry > count++)
			{
				show(cacheTime);
			}
			else
			{
				if ((e instanceof SocketTimeoutException || e instanceof IOException) && showDialog)
				{
					Dialog dialog = new AlertDialog.Builder(context).setMessage("网络连接超时 请退出重试")
							// 设置内容
							.setPositiveButton("退出",// 确定按钮
									new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog, int which)
										{
											dialog.dismiss();
											context.finish();
										}
									}).create();
					dialog.show();// 显示对话框
				}
			}

		}

	}

	/**
	 * 无网络弹出的提示窗口
	 */
	public void showDialog(String text)
	{
		if (text == null) text = "网络连接失败，请检查重试！";

		Dialog dialog = new AlertDialog.Builder(context).setMessage(text)// 设置内容
				.setPositiveButton("退出",// 确定按钮
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								context.finish();
							}
						}).setNegativeButton("设置", // 設置按钮
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int whichButton)
							{
								SysIntentUtil.goSettings(context);
								context.finish();
							}
						}).create();// 创建
		dialog.show();// 显示对话框
	}

}
