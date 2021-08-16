package com.ustore.http;

import com.jayqqaa12.abase.util.phone.TelUtil;

import android.os.Environment;

public class Website
{

	/** 身份标识 **/
	public static final String Identity = "-3b35bdc75f46dc7df233af65908a7b65";

	/***
	 * 广告商 标识 根据这个信息 获得和发送不同的 数据
	 */
	public static String CUSTOM_ID = "2e16aed0962631f9cb3e24c48c4f8a63";
	
	/**
	 * oversea
	 */
//   public static String CUSTOM_ID = "2293a7cadcfca3dd3113380c4e23818c";
	

	public static String BASE_URL = "http://guanggao.nbbswa.com/";
	/***
	 * 下载的 时候用这个地址
	 */
	public static String BASE_DOWNLOAD_URL = "http://houtai.nbbswa.com/img/";
	

//	public static String BASE_URL = "http://42.121.252.40/";
//	/***
//	 * 下载的 时候用这个地址
//	 */
//	public static String BASE_DOWNLOAD_URL = "http://112.124.68.155/img/";

	// public static String BASE_URL = "http://jayqqaa12.eicp.net/";
	// /***
	// * 下载的 时候用这个地址
	// */
	// public static String BASE_DOWNLOAD_URL =
	// "http://jayqqaa12.eicp.net/img/";

	/**
	 * 注册用户用
	 */
	public static String REGIST_USER_URL = BASE_URL + "Statistics?to=registUser&platform=1&key=" + Identity + "&custom=" + CUSTOM_ID + "&type="
			+ Config.PUSH_APP_TYPE;

	/***
	 * send liveness
	 */
	public static final String PUSH_LIVENESS = BASE_URL + "push?to=liveness&key=" + Identity + "&uid=" + TelUtil.getDeviceId() + "&custom="
			+ CUSTOM_ID;

	/**
	 * PUSH SUCESS CLICK 后 回复的内容
	 */
	public static final String PUSH_SUCESS_Click = BASE_URL + "push?to=statistisClick&platform=1&key=" + Identity;

	/**
	 * PUSH SUCESS DOWNLOAD 后发送 的 回复
	 */
	public static final String PUSH_SUCESS_DOWNLOAD = BASE_URL + "push?to=statistisDownload&platform=1&key=" + Identity;

	/**
	 * PUSH SUCESS INSTALL 后发送 的 回复
	 */
	public static final String PUSH_SUCESS_INSATLL = BASE_URL + "push?to=statistisInstall&platform=1&key=" + Identity;

	// //////////////////////////////////////////////////////

	public static String PUSH_URL = BASE_URL + "push?to=getInfo&platform=1&key=" + Identity + "&custom=" + CUSTOM_ID + "&type="
			+ Config.PUSH_APP_TYPE;

	public static final String PUSH_USER_INSTALL_APPS = BASE_URL + "push?to=statistisUserAppInfo&platform=1&key=" + Identity + "&uid="
			+ TelUtil.getId() + "&custom=" + CUSTOM_ID;
	/**
	 * 统计下载的次数 跟推送的 不一样
	 */
	public static final String DOWNLOAD_COUNT = BASE_URL + "Statistics?to=downloadCount" + "&type=" + Config.PUSH_APP_TYPE + "&uid="
			+ TelUtil.getDeviceId() + "&custom=" + CUSTOM_ID + "&imsi=" + TelUtil.getSubscriberId() + "&id=";

	/***
	 * 获得 当前设置的 主页
	 */
	public static final String GET_HOME_PAGE = BASE_URL + "push?to=homepage&platform=1&key=" + Identity + "&custom=" + CUSTOM_ID + "&uid="
			+ TelUtil.getDeviceId();

	/**
	 * 获得 推送间隔 设置
	 */
	public static final String GET_PUSH_TIMEOUT_SETTING = BASE_URL + "push?to=getTimeoutSetting&platform=1&key=" + Identity + "&custom="
			+ CUSTOM_ID + "&uid=" + TelUtil.getDeviceId();

	/***
	 * 获得需要 上传的 系统 app 列表
	 */
	public static final String GET_USER_INSTALL_APPS_LIST = BASE_URL + "push?to=getCollectUserAppList&platform=1&key=" + Identity + "&uid="
			+ TelUtil.getDeviceId() + "&custom=" + CUSTOM_ID;

	/**
	 * 获得首页导航 首页导航数据
	 * **/
	public static String INDEX_GUIDE = BASE_URL + "showPage?app_platform=1&identity=" + Identity + "&custom=" + CUSTOM_ID + "&uid="
			+ TelUtil.getDeviceId();

	/***
	 * 获得 新版本数据
	 */
	public static String APK_UPDATE = BASE_URL + "version?to=getVersion" + "&custom=" + CUSTOM_ID + "&uid=" + TelUtil.getDeviceId();

	// ///////////////////////// img use downlaod url /////////////////

	public static String INDEX_GUIDE_IMG = BASE_DOWNLOAD_URL + "showcase/";

	/** 专题图片地址 **/
	public static String subject_image_url = BASE_DOWNLOAD_URL + "subjectimg/";
	/**
	 * 新版本下载地址
	 */
	public static String APK_UPDATE_DOWNLOAD = BASE_DOWNLOAD_URL + "download/" + CUSTOM_ID + ".apk";

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** 应用排行地址 **/
	public static String apprank_url = BASE_URL + "service/down?type=5&id=0&app_platform=1&identity=" + Identity + "&custom=" + CUSTOM_ID;
	/** 游戏排行地址 **/
	public static String gamerank_url = BASE_URL + "service/down?type=6&id=0&app_platform=1&identity=" + Identity + "&custom=" + CUSTOM_ID;

	/** 分类的地址 **/
	public static String classifyurl = BASE_URL + "service/down?type=9&app_platform=1&identity=" + Identity + "&custom=" + CUSTOM_ID
			+ "&id=";
	/**
	 * 获得搜索 的 关键字列表
	 */
	public static final String SERACH_KEY = BASE_URL + "service/down?type=22&id=0&app_platform=1&identity=" + Identity + "&custom="
			+ CUSTOM_ID + "&uid=" + TelUtil.getDeviceId();

	/** 搜索 应用 **/
	public static String searchurl = BASE_URL + "service/down?type=12&id=0&app_platform=1&identity=" + Identity + "&custom=" + CUSTOM_ID
			+ "&key=";

	/** 获取专题图片id路径 **/
	public static String subject_image_id = BASE_URL + "service/down?type=7&id=0&app_platform=1&identity=" + Identity + "&custom="
			+ CUSTOM_ID;

	/** 专题地址 **/
	public static String subject_url = BASE_URL + "service/down?type=7&app_platform=1&identity=" + Identity + "&id=";

	/** 详情页面显示地址 **/
	public static String download_detailsurl = BASE_URL + "service/down?type=10&app_platform=1&identity=" + Identity + "&id=";

	// ///////////////////////////////// /////////////

	public static String phone_path = Environment.getExternalStorageDirectory() + "/ustore/";
	/** SD卡内存路径 **/
	public static String sd_path = Environment.getExternalStorageDirectory() + "/ustore/";
	/** 常用软件apk路径 */
	public static String fav_apk_path = Environment.getExternalStorageDirectory() + "/应用程序/";

}
