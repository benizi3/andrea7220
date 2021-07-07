package com.jayqqaa12.abase.util.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DownLoadUtil
{
	
	
	/**
	 * 获得 网络 图片
	 * 
	 * 非线程 需要 放在线程中使用
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getImage(String path) throws IOException
	{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		return BitmapFactory.decodeStream(is);
	}

	
	/**
	 * 下载指定的文本内容
	 * 
	 * @param url
	 *            请求下载的地址。
	 */
	public static String downloadString(String url) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
