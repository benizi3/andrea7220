package com.jayqqaa12.abase.util.network;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.jayqqaa12.abase.core.AbaseUtil;
import com.jayqqaa12.abase.util.common.Provider;

/**
 * 1.点击"Network"将输出本机所处的网络环境。 2.点击"WAP"将设定 移动网络接入点为CMWAP。 3.点击"GPRS"将设定
 * 移动网络接入点为CMNET。 注：自定义移动网络接入点的前提是“设置”→“无线和网络”→“移动网络”处已打勾。
 */
public class ApnUtil extends AbaseUtil
{

	/***
	 * 设置 中国移动的接入点 为 cmnet
	 */
	public static void setCmNetApn()
	{

		ContentValues cvGPRS = new ContentValues();
		cvGPRS.put("name", "GPRS");
		cvGPRS.put("apn", "cmnet");
		cvGPRS.put("type", "default");
		cvGPRS.put("mmsprotocol", "2.0");
		cvGPRS.put("mcc", "460");
		cvGPRS.put("mnc", "02");
		cvGPRS.put("numeric", "46002");
	}

	/**
	 * 设置 中国移动的 接入点 为 cmwap
	 */
	public static void setCmWapApn()
	{
		ContentValues cvWAP = new ContentValues();
		cvWAP.put("name", "移动梦网");
		cvWAP.put("apn", "cmwap");
		cvWAP.put("type", "default");
		cvWAP.put("proxy", "10.0.0.172");
		cvWAP.put("port", "80");
		cvWAP.put("mmsproxy", "10.0.0.172");
		cvWAP.put("mmsport", "80");
		cvWAP.put("mmsprotocol", "2.0");
		cvWAP.put("mmsc", "http://mmsc.monternet.com");
		cvWAP.put("mcc", "460");
		cvWAP.put("mnc", "02");
		cvWAP.put("numeric", "46002");

		setDefaultAPN(cvWAP);

	}

	public static void setUniNet()
	{
		ContentValues uniNet = new ContentValues();
		uniNet.put("name", "中国联通");
		uniNet.put("apn", "uninet");
		uniNet.put("type", "default");
		uniNet.put("mmsprotocol", "2.0");
		uniNet.put("mcc", "460");
		uniNet.put("mnc", "01");
		uniNet.put("numeric", "46001");

	}

	/**
	 * 
	 * 判断 当前 apn 是否 为 wap
	 * 
	 */
	public static boolean isWapApn()
	{
		boolean result = false;

		ContentResolver contentResolver = getContext().getContentResolver();
		Cursor cursor = contentResolver.query(Provider.PREFERRED_APN_URI, null, null, null, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				String apn = cursor.getString(cursor.getColumnIndex("apn"));
				if(apn.contains("wap")) result= true;
			}
		}
		cursor.close();
		
		return result;
	}

	private static void setDefaultAPN(ContentValues value)
	{
		int _id = findAPNId(value);
		if (_id == -1)
		{
			_id = insertAPN(value);
		}

		ContentValues values = new ContentValues();
		values.put("apn_id", _id);
		ContentResolver resolver = getContext().getContentResolver();
		resolver.update(Provider.PREFERRED_APN_URI, values, null, null);
	}

	private static int findAPNId(ContentValues cv)
	{
		int id = -1;
		ContentResolver contentResolver = getContext().getContentResolver();
		Cursor cursor = contentResolver.query(Provider.ALL_APN_URI, null, null, null, null);
		if (cursor != null)
		{
			while (cursor.moveToNext())
			{
				if (cursor.getString(cursor.getColumnIndex("name")).equals(cv.get("name"))
						&& cursor.getString(cursor.getColumnIndex("apn")).equals(cv.get("apn"))
						&& cursor.getString(cursor.getColumnIndex("numeric")).equals(cv.get("numeric")))
				{
					id = cursor.getShort(cursor.getColumnIndex("_id"));
					break;
				}
			}
		}

		cursor.close();

		return id;
	}

	private static int insertAPN(ContentValues value)
	{
		int apn_Id = -1;
		ContentResolver resolver = getContext().getContentResolver();

		Uri newRow = resolver.insert(Provider.ALL_APN_URI, value);
		if (newRow != null)
		{
			Cursor cursor = resolver.query(newRow, null, null, null, null);
			int idIdx = cursor.getColumnIndex("_id");
			cursor.moveToFirst();
			apn_Id = cursor.getShort(idIdx);

			cursor.close();
		}

		return apn_Id;
	}

}
