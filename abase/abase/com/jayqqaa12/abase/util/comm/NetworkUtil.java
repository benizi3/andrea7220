package com.jayqqaa12.abase.util.comm;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Environment;

import com.jayqqaa12.abase.core.AbaseUtil;
import com.jayqqaa12.abase.util.ManageUtil;

public class NetworkUtil extends AbaseUtil
{

	/**
	 * 获得 网络 图片
	 * 
	 * 非线程
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Bitmap getImage(String path) throws Exception
	{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		return BitmapFactory.decodeStream(is);
	}


	/**
	 * 又缓存
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable loadImage(String url, String cachePath)
	{
		Drawable d = null;
		try
		{
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			{// 是否有SD卡的读取权限
				File file = new File(cachePath);
				if (!file.exists())
				{
					file.mkdirs();
				}
				String fileName = url.substring(cachePath.length(), url.length()).replaceAll("\\/", "");
				File f = new File(cachePath, fileName);
				fileName = null;

				if (f.exists())
				{

					FileInputStream fis = new FileInputStream(f);
					d = Drawable.createFromStream(fis, "src");

					if (d == null)
					{

						f.delete();
						// 创建文件写到SD卡
						f.createNewFile();
						URL m = new URL(url);
						InputStream i = (InputStream) m.getContent();
						DataInputStream in = new DataInputStream(i);
						FileOutputStream out = new FileOutputStream(f);
						byte[] buffer = new byte[256];
						int byteread = 0;
						while ((byteread = in.read(buffer)) != -1)
						{
							out.write(buffer, 0, byteread);
						}
						in.close();
						out.close();
						FileInputStream fis2 = new FileInputStream(f);
						d = Drawable.createFromStream(fis2, "src");
						i.close();
						fis2.close();
					}
					fis.close();

				}
				else
				{// 创建文件写到SD卡

					f.createNewFile();
					URL m = new URL(url);
					InputStream i = (InputStream) m.getContent();
					DataInputStream in = new DataInputStream(i);
					FileOutputStream out = new FileOutputStream(f);
					byte[] buffer = new byte[256];
					int byteread = 0;
					while ((byteread = in.read(buffer)) != -1)
					{
						out.write(buffer, 0, byteread);
					}
					in.close();
					out.close();
					FileInputStream fis = new FileInputStream(f);
					d = Drawable.createFromStream(fis, "src");
					i.close();
					fis.close();
				}
			}
			else
			{// 连接网络直接获取图片
				URL m = new URL(url);
				InputStream i = (InputStream) m.getContent();
				d = Drawable.createFromStream(i, "src");
				i.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return d;// 返回得到的图片
	}

	/**
	 * 判断 wifi 是否 连接
	 * 
	 * @param cm
	 * @return
	 */
	public static boolean isWifiConnecting(ConnectivityManager cm)
	{

		return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}

	/**
	 * 判断 wifi 是否 连接
	 * 
	 * @param cm
	 * @return
	 */
	public static boolean isWifiConnecting()
	{
		return ManageUtil.getConnectivtyManager().getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}

	/**
	 * 判断 mobile 网络 是否连接
	 * 
	 * @param cm
	 * @return
	 */
	public static boolean isMobileConnecting(ConnectivityManager cm)
	{
		return cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
	}

	/**
	 * 判断 mobile 网络 是否连接
	 * 
	 * @param cm
	 * @return
	 */
	public static boolean isMobileConnecting()
	{
		return ManageUtil.getConnectivtyManager().getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
	}

	/**
	 * 检测是否已经连接网络。
	 * 
	 * 需要 权限 android.permission.ACCESS_NETWORK_STATE.
	 * 
	 * @return 当且仅当连上网络时返回true,否则返回false。
	 */
	public static boolean isConnectingToInternet()
	{
		ConnectivityManager connectivityManager = ManageUtil.getConnectivtyManager();
		if (connectivityManager == null) { return false; }
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return (info != null) && info.isAvailable();
	}

	/**
	 * 获得 所有 网络的 流量 包括 发送和接受的
	 * 
	 * @return
	 */
	public static long getTotalBytes()
	{
		return getTotalRxBytes() + getTotalTxBytes();
	}

	/**
	 * 获取总的接受字节数，包含Mobile和WiFi等
	 * 
	 * @return
	 */
	public static long getTotalRxBytes()
	{
		return TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalRxBytes();
	}

	/**
	 * 总的发送字节数，包含Mobile和WiFi等
	 * 
	 * @return
	 */
	public static long getTotalTxBytes()
	{
		return TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalTxBytes();
	}

	/**
	 * Mobile总的发送和接受字节数，
	 * 
	 * @return
	 */

	public static long getTotalMobileBytes()
	{
		return getMobileRxBytes() + getMobileTxBytes();
	}

	/**
	 * wifi总的发送和接受字节数，
	 * 
	 * @return
	 */
	public static long getTotalWifiBytes()
	{
		return getTotalBytes() - getTotalMobileBytes();
	}

	/**
	 * 获取通过Mobile连接收到的字节总数，不包含WiFi
	 * 
	 */
	public static long getMobileRxBytes()
	{
		return TrafficStats.getMobileRxBytes() == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getMobileRxBytes();
	}

	/**
	 * 获取通过Mobile连接收到的字节总数，不包含WiFi
	 * 
	 */
	public static long getMobileTxBytes()
	{
		return TrafficStats.getMobileTxBytes() == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getMobileTxBytes();
	}

	// ////////////////////////---------------UID--------- ////////////////

	/**
	 * 获得 uid 所有 网络的 流量 包括 发送和接受的
	 * 
	 * @return
	 */
	public static long getUidTotalBytes(int uid)
	{
		return getUidRxBytes(uid) + getUidTxBytes(uid);
	}

	/**
	 * 获取uid 的接受字节数，包含Mobile和WiFi等
	 * 
	 * @return
	 */
	public static long getUidRxBytes(int uid)
	{
		return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getUidRxBytes(uid);
	}

	/**
	 * 获取uid 的发送字节数，包含Mobile和WiFi等
	 * 
	 * @return
	 */
	public static long getUidTxBytes(int uid)
	{
		return TrafficStats.getUidTxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getUidTxBytes(uid);
	}

}
