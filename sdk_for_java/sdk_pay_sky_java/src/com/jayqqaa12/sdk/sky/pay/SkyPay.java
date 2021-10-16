package com.jayqqaa12.sdk.sky.pay;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;

import com.skymobi.pay.sdk.SkyPayServer;
import com.skymobi.pay.sdk.SkyPaySignerInfo;
import com.skymobi.payjar.R;

public class SkyPay {
	
//	private static final String PAY_SERVER = "http://192.168.149.122:8888/jump-server/jump/api/sale/pay";

	//��������
	private static final String ORDER_INFO_PAY_METHOD = "payMethod";
	private static final String ORDER_INFO_SYSTEM_ID = "systemId";
	private static final String ORDER_INFO_CHANNEL_ID = "channelId";
	private static final String ORDER_INFO_PAY_POINT_NUM = "payPointNum";
	private static final String ORDER_INFO_ORDER_DESC = "orderDesc";
	private static final String ORDER_INFO_PRODUCT_NAME = "productName";
	private static final String ORDER_INFO_GAME_TYPE = "gameType";
	private static final String ORDER_INFO_NOTIFY_ADDRESS = "notifyAddress";

	private static String mOrderInfo = null;
	private static SkyPayHandle mPayHandler = null;
	private static SkyPayServer mSkyPayServer = null;
	
	
	

	public static void pay(Activity ACTIVITY, String PAYMETHOD,
			String MERCHANTPASSWD, String MERCHANTID,
			String APPID, String PAYPOINTNUM, String VERSION,
			String PRICE, String SYSTEMID, String CHANNELID,
			String PAYTYPE, String GAMETYPE, String APPNAME,SkyPayCallback callback,int type ) {
	 
		mPayHandler = new SkyPayHandle( callback ,type);
		mSkyPayServer = SkyPayServer.getInstance();
		int initRet = mSkyPayServer.init(mPayHandler);

		if (SkyPayServer.PAY_RETURN_SUCCESS == initRet) {
		} else {
		}
 
		String merchantPasswd = MERCHANTPASSWD;
		String merchantId = MERCHANTID;
		if (merchantId == null || merchantPasswd == null) {
			return;
		}
	 
		String appId = APPID;
		String paymethod = PAYMETHOD;
	 
		String orderId = SystemClock.elapsedRealtime() + "";
		String appName = APPNAME;
		String appVersion = VERSION;	//packInfo.versionCode + "";
		String systemId = SYSTEMID;
		String price = PRICE;
		String channelId = CHANNELID;
		String payType = PAYTYPE;

	 
//set callback service 		 
//		skyPaySignerInfo.setNotifyAddress(notifyAddress);
		SkyPaySignerInfo skyPaySignerInfo = new SkyPaySignerInfo();

		skyPaySignerInfo.setMerchantPasswd(merchantPasswd);
		skyPaySignerInfo.setMerchantId(merchantId);
		skyPaySignerInfo.setAppId(appId);
		skyPaySignerInfo.setAppName(appName);
		skyPaySignerInfo.setAppVersion(appVersion);
		skyPaySignerInfo.setPayType(payType);
		skyPaySignerInfo.setPrice(price);
		skyPaySignerInfo.setOrderId(orderId);
	 
		String reserved1 = "id";
		 
		skyPaySignerInfo.setReserved1(reserved1, false);
		
		String payPointNum = PAYPOINTNUM;
		String gameType = GAMETYPE;

		String signOrderInfo =  mSkyPayServer.getSignOrderString(skyPaySignerInfo);

		mOrderInfo = 
		ORDER_INFO_PAY_METHOD + "=" + paymethod + "&"
		+ ORDER_INFO_SYSTEM_ID + "=" + systemId + "&"
		+ ORDER_INFO_CHANNEL_ID +  "=" + channelId + "&"
		+ ORDER_INFO_PAY_POINT_NUM + "=" + payPointNum + "&"
		+ ORDER_INFO_GAME_TYPE + "=" + gameType + "&"
		+ ORDER_INFO_ORDER_DESC + "=" +ACTIVITY.getString(R.string.pay_des) +"&"
		+ signOrderInfo;
		int payRet = mSkyPayServer.startActivityAndPay(ACTIVITY, mOrderInfo);

    	if (SkyPayServer.PAY_RETURN_SUCCESS == payRet) {
    		Log.d("ShyPay","pay init success");
    	} else {
    		Log.d("ShyPay","pay init fail  result="+payRet);
    	}
	}
}