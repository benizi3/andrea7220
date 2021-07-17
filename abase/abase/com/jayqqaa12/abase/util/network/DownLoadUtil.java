package com.jayqqaa12.abase.util.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.jayqqaa12.abase.core.ACache;

public class DownLoadUtil
{

	/**
	 * 获得 网络 图片 非线程 需要 放在线程中使用
	 * 
	 * 带有acache缓存 不用考虑其他缓存
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmap(String url)
	{
		ACache cache = ACache.create();
		Bitmap bitmap = cache.getAsBitmap(url);

		if (bitmap == null)
		{
			try
			{
				URL u = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				cache.put(url, bitmap);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return bitmap;
	}
	
	
	
	/**
	 * 获得 网络 图片 非线程 需要 放在线程中使用
	 * 
	 * 带有acache缓存 不用考虑其他缓存
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Drawable getDrawable(String url)
	{
		ACache cache = ACache.create();
		Drawable dd = cache.getAsDrawable(url);

		if (dd == null)
		{
			try
			{
				URL u = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				InputStream is = conn.getInputStream();
				dd = Drawable.createFromStream(is, "src");
				cache.put(url, dd);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return dd;
	}

	/**
	 * 下载指定的文本内容
	 * 
	 * @param url
	 *            请求下载的地址。
	 */
	public static String downloadString(String url)
	{
		try
		{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			return EntityUtils.toString(response.getEntity());
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
