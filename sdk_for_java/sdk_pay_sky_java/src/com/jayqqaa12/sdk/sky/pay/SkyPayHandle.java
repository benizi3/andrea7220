package com.jayqqaa12.sdk.sky.pay;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.skymobi.pay.sdk.SkyPayServer;

public class SkyPayHandle extends Handler
{

	public static final String STRING_MSG_CODE = "msg_code";
	public static final String STRING_ERROR_CODE = "error_code";
	public static final String STRING_PAY_STATUS = "pay_status";
	public static final String STRING_PAY_PRICE = "pay_price";
	public static final String STRING_CHARGE_STATUS = "3rdpay_status";
	private SkyPayCallback callback;
	private int type;
	
 

	public SkyPayHandle(SkyPayCallback callback,int type)
	{
		this.callback = callback;
		this.type =type;
	}



	@Override
	public void handleMessage(Message msg)
	{
		
		super.handleMessage(msg);

		if (msg.what == SkyPayServer.MSG_WHAT_TO_APP)
		{
			String retInfo = (String) msg.obj;
			Map<String, String> map = new HashMap<String, String>();
			Log.d("ShyPay", retInfo);

			String[] keyValues = retInfo.split("&|=");
			for (int i = 0; i < keyValues.length; i = i + 2)
			{
				map.put(keyValues[i], keyValues[i + 1]);
			}

			int msgCode = Integer.parseInt(map.get(STRING_MSG_CODE));
			if (msgCode == 100)
			{
				if (map.get(STRING_PAY_STATUS) != null)
				{
					int payStatus = Integer.parseInt(map.get(STRING_PAY_STATUS));
					int payPrice = Integer.parseInt(map.get(STRING_PAY_PRICE));
					int errcrCode = 0;
					if (map.get(STRING_ERROR_CODE) != null) errcrCode = Integer.parseInt(map.get(STRING_ERROR_CODE));

					switch (payStatus)
					{
					case 102:
						Log.d("ShyPay", "pay success  payprice=" + payPrice);

						callback.success(type);
						
						
						break;
					case 101:
						Log.d("ShyPay", "pay fail error code=" + errcrCode);
						final int code = errcrCode;

						callback.fail(type);
						
					}
				}
			}
			else
			{
				int errcrCode = Integer.parseInt(map.get(STRING_ERROR_CODE));
				Log.d("ShyPay", "pay fail error code=" + errcrCode);
			}
		}
	}
}
