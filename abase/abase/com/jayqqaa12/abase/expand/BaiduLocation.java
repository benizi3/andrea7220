package com.jayqqaa12.abase.expand;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jayqqaa12.abase.core.Abase;


/***
 * 
 * 百度 定位 sdk
 * 
 * 使用方法 ：
 * 
 * 调用 initLocationClinet  传入  Listenre 
 * 然后 即可 
 * 不用的话 应该 停止  stop
 * 
 * 使用 前 必需 注册 服务     和 加入  jar 和 so 
 *  <service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:process=":remote" >
        </service>
 * 
 * @author 12
 *
 */
public class BaiduLocation  
{
	public static LocationClient mLocationClient = null;

	public static LocationClient initLocationClient(BDLocationListener myListener)
	{
		if (mLocationClient == null) mLocationClient = new LocationClient(Abase.getContext());

		mLocationClient.registerLocationListener(myListener);

		setOption(5000, true, true, true);
		
		mLocationClient.start();
		mLocationClient.requestLocation();
		
		return mLocationClient;
	}
	
	
	/**
	 * set option 
	 * @param space
	 * @param getLocation
	 * @param disableCache
	 * @param openGps
	 */
	public static void setOption(int space ,boolean getLocation,boolean disableCache,boolean openGps)
	{
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(openGps); // disable gps 
		option.setServiceName("com.baidu.location.service_v2.9");
//		option.setPoiExtraInfo(true);
//		option.setPoiNumber(100);
		if(getLocation)option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst); // network first 
		option.setScanSpan(space);
		option.disableCache(disableCache);
		mLocationClient.setLocOption(option);
	}
	
	
	
	
	/**
	 * stop baidu location 
	 */
	public static void stop()
	{
		mLocationClient.stop();
	}
	

	
	
	
	

}
