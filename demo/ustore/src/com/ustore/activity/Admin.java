package com.ustore.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ustore.adapter.DownloadAdapter;
import com.ustore.adapter.InstalledAdapter;
import com.ustore.download.DBHelper;
import com.ustore.download.Dao;
import com.ustore.http.Website;
import com.ustore.service.DownloadService;

@SuppressLint("HandlerLeak")
public class Admin extends Activity
{

	private boolean isrun = false;

	private ArrayList<Map<String, Object>> data = null;
	/** 0表示已经安装，且跟现在这个APK文件是一个版本 **/
	private static int INSTALLED = 0;
	/** 1表示未安装 **/
	private static int UNINSTALLED = 1;


	private ViewPager mPager;
	private List<View> Views; 
	private ImageView cursor;
	private TextView t1, t2, t3;

	/**
	 * if install list is empty or download list
	 */
	private ImageView iv_install, iv_download;

	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;

	ListView list1, list2;
	Resources res = null;
	ProgressBar p1;

	private static final String TAG_NAME = "管理";
	TextView title;
	ImageView top1;
	DownloadAdapter adapter = null;
	InstalledAdapter adapter2;
	SQLiteDatabase db;
	
	private int pos;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admini);
		title = (TextView) findViewById(R.id.t1);
		title.setText(TAG_NAME);
		top1 = (ImageView) findViewById(R.id.top1);
		prepareView();

		IntentFilter filter = new IntentFilter();
		filter.addAction("broadcast.adminupdate");
		registerReceiver(adminUpdateReceiver, filter);
		
		pos = getIntent().getIntExtra("page", 0);

		db = new DBHelper(getApplicationContext()).getReadableDatabase();
		
		
		mPager.setCurrentItem(pos);

	}

	/**
	 * 下载apk完成时，更新管理界面
	 */
	private BroadcastReceiver adminUpdateReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			Intent intent2 = new Intent();// 刷新
			intent2.setClass(Admin.this, Admin.class);
			startActivity(intent2);
			finish();
			overridePendingTransition(0, 0);// 禁用跳转动画效果

		}

	};

	@Override
	protected void onResume()
	{
		super.onResume();
		if (Home.isDownLoading)
		{
			top1.setVisibility(View.VISIBLE);
		}
		else
		{
			top1.setVisibility(View.GONE);
		}

	}

	/** 实例化按钮 **/
	private void prepareView()
	{
		isrun = true;
		res = getResources();
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(res, R.drawable.line).getWidth();// 鑾峰彇鍥剧墖瀹藉害
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 鑾峰彇鍒嗚鲸鐜囧搴?
		offset = (screenW / 2 - bmpW) / 2;// 璁＄畻鍋忕Щ閲?
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 璁剧疆鍔ㄧ敾鍒濆浣嶇疆
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t2.setTextColor(res.getColor(R.color.disable));

		mPager = (ViewPager) findViewById(R.id.vPager);
		Views = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		Views.add(mInflater.inflate(R.layout.lay2, null));
		Views.add(mInflater.inflate(R.layout.lay2, null));
		mPager.setAdapter(new MyPagerAdapter(Views));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		isrun = false;

		unregisterReceiver(adminUpdateReceiver);

		if (db != null)
		{
			db.close();

		}

	}

	public class MyPagerAdapter extends PagerAdapter
	{
		private List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews)
		{
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2)
		{
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0)
		{}

		@Override
		public int getCount()
		{
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1)
		{

			if (arg1 < 2)
			{
				((ViewPager) arg0).addView(mListViews.get(arg1 % 2), 0);
			}

			if (arg1 == 0)
			{
				list1 = (ListView) arg0.findViewById(R.id.list2);

				iv_download = (ImageView) arg0.findViewById(R.id.iv);
				iv_download.setImageResource(R.drawable.download_empty);

				new UpdateThread().start();

			}
			else if (arg1 == 1)
			{
				list2 = (ListView) arg0.findViewById(R.id.list2);

				p1 = (ProgressBar) arg0.findViewById(R.id.p1);
				p1.setVisibility(View.VISIBLE);
				iv_install = (ImageView) arg0.findViewById(R.id.iv);
				iv_install.setImageResource(R.drawable.install_empty);

				begin();

			}

			return mListViews.get(arg1 % 2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1)
		{}

		@Override
		public Parcelable saveState()
		{
			return null;
		}

		@Override
		public void startUpdate(View arg0)
		{}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener
	{

		int one = offset * 2 + bmpW;
		int two = one * 2;

		public void onPageSelected(int arg0)
		{
			Animation animation = null;
			switch (arg0)
			{
			case 0:
				if (currIndex == 1)
				{
					t1.setTextColor(Color.BLACK);
					t2.setTextColor(res.getColor(R.color.disable));
					animation = new TranslateAnimation(one, 0, 0, 0);

					currIndex = arg0;

				}
				break;

			case 1:
				if (currIndex == 0)
				{
					t1.setTextColor(res.getColor(R.color.disable));
					t2.setTextColor(Color.BLACK);
					animation = new TranslateAnimation(offset, one, 0, 0);

					currIndex = arg0;

				}
				break;
			}

			currIndex = arg0;
			animation.setFillAfter(true); // True:鍥剧墖鍋滃湪鍔ㄧ敾缁撴潫浣嶇疆
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		public void onPageScrollStateChanged(int arg0)
		{

		}
	}

	public class MyOnClickListener implements View.OnClickListener
	{
		private int index = 0;

		public MyOnClickListener(int i)
		{
			index = i;
		}

		public void onClick(View v)
		{

			if (index == 0)
			{
				t1.setTextColor(Color.BLACK);
				t2.setTextColor(res.getColor(R.color.disable));
			}
			else
			{
				t1.setTextColor(res.getColor(R.color.disable));
				t2.setTextColor(Color.BLACK);
			}

			mPager.setCurrentItem(index);
		}
	};

	public void onClickBar(View v)
	{
		switch (v.getId())
		{

		// 搜索
		case R.id.top2:
			startActivity(new Intent(this, Search.class));

			break;

		// 管理
		case R.id.top3:
			startActivity(new Intent(this, Admin.class));
			break;

		}

		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

	}

	/**
	 * 更新进度
	 */
	public class UpdateThread extends Thread
	{

		public void run()
		{

			while (isrun)
			{

				WeakHashMap<String, String[]> listdatass = null;
				Cursor cursor = null;

				try
				{
					int keyindex = 0;
					listdatass = new WeakHashMap<String, String[]>();
					cursor = Dao.getInstance(getApplicationContext()).queryAllDownloadList(db);

					if (cursor != null)
					{

						while (cursor.moveToNext())
						{
							// state=0 下载中 state=1 暂停 state=2 等待
							String state = null;

							// 下载此id对应apk的线程在下载队列中
							if (DownloadService.downloadSet.contains(cursor.getString(0)))
							{

								// 正在下载
								if (DownloadService.downloadingSet.contains(cursor.getString(0)))
								{
									state = "0";

									// 在下载队列中，当前没有在下载
								}
								else
								{
									state = "2";
								}

							}
							else
							{
								state = "1";
							}

							listdatass.put(String.valueOf(keyindex),
									new String[] { cursor.getString(1), cursor.getString(3) + "%", cursor.getString(3),
											cursor.getString(0), cursor.getString(2), state });
							keyindex = keyindex + 1;
						}
					}

					Message msg = Message.obtain();
					msg.obj = listdatass;
					msg.what = 1;
					handler.sendMessage(msg);

					Thread.sleep(1000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (cursor != null)
					{
						cursor.close();
						cursor = null;
					}
				}
			}

		}
	}

	private myhandler handler = new myhandler(this);

	/** 执行消息并执行相关操作 **/
	private class myhandler extends Handler
	{
		WeakReference<Admin> myadmin;

		myhandler(Admin admin)
		{
			myadmin = new WeakReference<Admin>(admin);
		}

		public void handleMessage(Message msg)
		{

			Admin adm = myadmin.get();
			if (adm == null) { return; }

			switch (msg.what)
			{

			// 下载列表
			case 1:

				WeakHashMap<String, String[]> listd = (WeakHashMap<String, String[]>) msg.obj;

				if (adapter == null)
				{
					adapter = new DownloadAdapter(adm, listd, R.layout.download_listview, new String[] { "name", "percent", "progress",
							"icon" }, new int[] { R.id.tv_notify, R.id.notity_percent, R.id.pb_notify, R.id.iv_notify });
					list1.setAdapter(adapter);
					if (adapter.getCount() == 0) iv_download.setVisibility(View.VISIBLE);

				}
				else
				{
					adapter.setData(listd);
					if (adapter.getCount() == 0) iv_download.setVisibility(View.VISIBLE);
					else iv_download.setVisibility(View.INVISIBLE);
				}

				break;

			// 安装列表
			case 2:
				adapter();
				p1.setVisibility(View.GONE);

				break;

			}
		};
	};

	/**
	 * 开启线程，显示进度条，准备获取sd卡文件中是否有安装包
	 */
	private void begin()
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					getApkinfo();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				handler.sendEmptyMessage(2);
			}

			/**
			 * 读取安装包信息
			 */
			private void getApkinfo()
			{
				try
				{
					StringBuffer mSavePath = new StringBuffer(Website.sd_path);
					mSavePath.append("download");// 安装包路径
					File dir = new File(mSavePath.toString());
					File[] files = dir.listFiles();
					data = new ArrayList<Map<String, Object>>();
					if (files != null)
					{

						String apk_path = null;// 安装包的路径
						String name = null;// 安装包的名字
						PackageManager pm = getPackageManager();// 安装包管理器初始化
						PackageInfo info = null;// 安装包信息
						ApplicationInfo appInfo = null;// 安装包版本信息管理器
						String packageName = null;// 安装包包名
						StringBuffer versionname = null;// 安装包版本信息
						int versioncode = 0;// 版本号
						String PATH_PackageParser = null;
						String PATH_AssetManager = null;

						Drawable icon = null;// 图片

						System.out.println("apk count:" + files.length);

						for (int i = 0; i < files.length; ++i)
						{
							WeakHashMap<String, Object> map = new WeakHashMap<String, Object>();
							if (files[i].exists())
							{
								name = files[i].getName();// 安装包的名称
								if (name.endsWith(".apk"))
								{
									apk_path = files[i].getAbsolutePath();// apk文件的绝对路劲
									// pm = getPackageManager();

									try
									{

										info = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
									}
									catch (Exception e)
									{}
									if (info != null)
									{

										CharSequence label = null;// 名字

										appInfo = info.applicationInfo;
										// String appName =
										// pm.getApplicationLabel(appInfo).toString();

										packageName = appInfo.packageName; // 得到安装包的包名
										versionname = new StringBuffer("版本：");
										versionname.append(info.versionName); // 得到版本信息
										versioncode = info.versionCode;
										// Drawable icon
										// =pm.getApplicationIcon(appInfo);//得到图标信息
										// Drawable icon =
										// info.applicationInfo.loadIcon(getPackageManager());

										PATH_PackageParser = "android.content.pm.PackageParser";
										PATH_AssetManager = "android.content.res.AssetManager";
										try
										{// 获取安装包的图标和显示的名称

											Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
											Class<?>[] typeArgs = new Class[1];
											typeArgs[0] = String.class;
											Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
											Object[] valueArgs = new Object[1];
											valueArgs[0] = apk_path;
											Object pkgParser = pkgParserCt.newInstance(valueArgs);

											DisplayMetrics metrics = new DisplayMetrics();

											typeArgs = new Class[4];
											typeArgs[0] = File.class;
											typeArgs[1] = String.class;
											typeArgs[2] = DisplayMetrics.class;
											typeArgs[3] = Integer.TYPE;
											Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
											valueArgs = new Object[4];
											valueArgs[0] = new File(apk_path);
											valueArgs[1] = apk_path;
											valueArgs[2] = metrics;
											valueArgs[3] = 0;
											Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
											// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开

											Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
											ApplicationInfo infor = (ApplicationInfo) appInfoFld.get(pkgParserPkg);

											Class<?> assetMagCls = Class.forName(PATH_AssetManager);
											Constructor<?> assetMagCt = assetMagCls.getConstructor((Class[]) null);
											Object assetMag = assetMagCt.newInstance((Object[]) null);
											typeArgs = new Class[1];
											typeArgs[0] = String.class;
											Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
											valueArgs = new Object[1];
											valueArgs[0] = apk_path;
											assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
											Resources res = getResources();
											typeArgs = new Class[3];
											typeArgs[0] = assetMag.getClass();
											typeArgs[1] = res.getDisplayMetrics().getClass();
											typeArgs[2] = res.getConfiguration().getClass();
											Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);
											valueArgs = new Object[3];
											valueArgs[0] = assetMag;
											valueArgs[1] = res.getDisplayMetrics();
											valueArgs[2] = res.getConfiguration();
											res = (Resources) resCt.newInstance(valueArgs);

											assetMag_addAssetPathMtd = null;
											typeArgs = null;
											valueArgs = null;
											metrics = null;
											pkgParserCt = null;
											pkgParserPkg = null;
											pkgParserCls = null;
											pkgParser = null;
											pkgParser_parsePackageMtd = null;
											appInfoFld = null;
											assetMagCls = null;
											assetMagCt = null;
											if (infor.labelRes != 0)
											{// 获得安装包的显示名称
												label = res.getText(infor.labelRes);
											}

											if (label == null)
											{
												label = "无此apk标签信息";
											}

											// 读取一个apk程序的图标
											if (infor.icon != 0)
											{
												icon = res.getDrawable(infor.icon);

											}
											infor = null;
											res = null;
										}
										catch (Exception e)
										{
											e.printStackTrace();
										}
										int type = doType(pm, packageName, versioncode);// 得到安装文件的安装类型

										if (type == 0)
										{// 软件已安装

											map.put("button", R.drawable.uninstall);
											map.put("type", "0");
										}
										else
										{// 软件未安装
											map.put("button", R.drawable.install);
											map.put("type", "1");
										}

										map.put("appname", label);
										map.put("version", versionname.toString());
										map.put("appicon", icon);
										map.put("apkname", name);
										map.put("package", packageName);

										map.put("tag", "1");

										System.out.println("label:" + label + "===package:" + packageName);

										data.add(map);
									}

								}

							}
						}
						versioncode = 0;
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				// add by chris 扫描 常用软件文件夹下的apk
				try
				{

					StringBuffer mSavePath = new StringBuffer(Website.fav_apk_path);
					File dir = new File(mSavePath.toString());
					File[] files = dir.listFiles();

					if (files != null)
					{

						String apk_path = null;// 安装包的路径
						String name = null;// 安装包的名字
						PackageManager pm = getPackageManager();// 安装包管理器初始化
						PackageInfo info = null;// 安装包信息
						ApplicationInfo appInfo = null;// 安装包版本信息管理器
						String packageName = null;// 安装包包名
						StringBuffer versionname = null;// 安装包版本信息
						int versioncode = 0;// 版本号
						String PATH_PackageParser = null;
						String PATH_AssetManager = null;

						Drawable icon = null;// 图片

						System.out.println("apk count:" + files.length);

						for (int i = 0; i < files.length; ++i)
						{
							WeakHashMap<String, Object> map = new WeakHashMap<String, Object>();
							if (files[i].exists())
							{
								name = files[i].getName();// 安装包的名称
								if (name.endsWith(".apk"))
								{
									apk_path = files[i].getAbsolutePath();// apk文件的绝对路劲
									// pm = getPackageManager();

									try
									{

										info = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
									}
									catch (Exception e)
									{}
									if (info != null)
									{

										CharSequence label = null;// 名字

										appInfo = info.applicationInfo;
										// String appName =
										// pm.getApplicationLabel(appInfo).toString();

										packageName = appInfo.packageName; // 得到安装包的包名
										versionname = new StringBuffer("版本：");
										versionname.append(info.versionName); // 得到版本信息
										versioncode = info.versionCode;
										// Drawable icon
										// =pm.getApplicationIcon(appInfo);//得到图标信息
										// Drawable icon =
										// info.applicationInfo.loadIcon(getPackageManager());

										PATH_PackageParser = "android.content.pm.PackageParser";
										PATH_AssetManager = "android.content.res.AssetManager";
										try
										{// 获取安装包的图标和显示的名称

											Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
											Class<?>[] typeArgs = new Class[1];
											typeArgs[0] = String.class;
											Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
											Object[] valueArgs = new Object[1];
											valueArgs[0] = apk_path;
											Object pkgParser = pkgParserCt.newInstance(valueArgs);

											DisplayMetrics metrics = new DisplayMetrics();

											typeArgs = new Class[4];
											typeArgs[0] = File.class;
											typeArgs[1] = String.class;
											typeArgs[2] = DisplayMetrics.class;
											typeArgs[3] = Integer.TYPE;
											Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
											valueArgs = new Object[4];
											valueArgs[0] = new File(apk_path);
											valueArgs[1] = apk_path;
											valueArgs[2] = metrics;
											valueArgs[3] = 0;
											Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
											// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开

											Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
											ApplicationInfo infor = (ApplicationInfo) appInfoFld.get(pkgParserPkg);

											Class<?> assetMagCls = Class.forName(PATH_AssetManager);
											Constructor<?> assetMagCt = assetMagCls.getConstructor((Class[]) null);
											Object assetMag = assetMagCt.newInstance((Object[]) null);
											typeArgs = new Class[1];
											typeArgs[0] = String.class;
											Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
											valueArgs = new Object[1];
											valueArgs[0] = apk_path;
											assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
											Resources res = getResources();
											typeArgs = new Class[3];
											typeArgs[0] = assetMag.getClass();
											typeArgs[1] = res.getDisplayMetrics().getClass();
											typeArgs[2] = res.getConfiguration().getClass();
											Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);
											valueArgs = new Object[3];
											valueArgs[0] = assetMag;
											valueArgs[1] = res.getDisplayMetrics();
											valueArgs[2] = res.getConfiguration();
											res = (Resources) resCt.newInstance(valueArgs);

											if (infor.labelRes != 0)
											{// 获得安装包的显示名称
												label = res.getText(infor.labelRes);
											}

											if (label == null)
											{
												label = "无此apk标签信息";
											}

											// 读取一个apk程序的图标
											if (infor.icon != 0)
											{
												icon = res.getDrawable(infor.icon);

											}
											infor = null;
											res = null;
										}
										catch (Exception e)
										{
											e.printStackTrace();
										}
										int type = doType(pm, packageName, versioncode);// 得到安装文件的安装类型

										if (type == 0)
										{// 软件已安装

											map.put("button", R.drawable.uninstall);
											map.put("type", "0");
										}
										else
										{// 软件未安装
											map.put("button", R.drawable.install);
											map.put("type", "1");
										}

										map.put("appname", label);
										map.put("version", versionname.toString());
										map.put("appicon", icon);
										map.put("apkname", name);
										map.put("package", packageName);

										// tag=1 download文件夹 tag=2 常用软件文件夹
										map.put("tag", "2");

										System.out.println("label:" + label + "===package:" + packageName);

										data.add(map);
									}

								}

							}
						}
						versioncode = 0;
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}.start();
	}

	/** 读取安装包信息和系统已经安装的软件信息对比来返回读取的软件是否安装 ***/
	private int doType(PackageManager pm, String packageName, int versionCode)
	{
		List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

		String pi_packageName = null;
		int pi_versionCode;
		for (PackageInfo pi : pakageinfos)
		{
			pi_packageName = pi.packageName;
			pi_versionCode = pi.versionCode;

			// System.out.println("pack name:"+pm.getApplicationLabel(pi.applicationInfo));

			// 如果这个包名在系统已经安装过的应用中存在
			if (packageName.endsWith(pi_packageName))
			{
				// Log.i("test","此应用安装过了");
				if (versionCode == pi_versionCode)
				{
					// Log.i("test","已经安装，不用更新，可以卸载该应用");
					return INSTALLED;
				}
			}
		}

		return UNINSTALLED;

	}

	/** 适配安装列表要显示的是否安装的软件信息 **/
	private void adapter()
	{
		adapter2 = new InstalledAdapter(this, data, R.layout.install_listview, new String[] { "appname", "version", "appicon", "button",
				"apkname", "type", "package", "tag" }, new int[] { R.id.appname, R.id.appversion, R.id.appicon, R.id.appbutton,
				R.id.apptext, R.id.apptext, R.id.apptext, R.id.apptext });

		list2.setAdapter(adapter2);
		adapter2.setActivity(this);

		if (adapter2.getCount() == 0) iv_install.setVisibility(View.VISIBLE);
		else iv_install.setVisibility(View.INVISIBLE);

	}

	/**
	 * 点击Menu菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, 1, 1, "退出");
		menu.add(0, 2, 2, "刷新");
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 选择menu菜单的选型
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case 1:
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			System.exit(0);// 退出程序
			break;
		case 2:
			Intent intent = new Intent();// 刷新
			intent.setClass(this, Admin.class);
			startActivity(intent);
			finish();
			overridePendingTransition(0, 0);// 禁用跳转动画效果
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 点击返回按钮退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{// 点击back键

			finish();// 退出程序
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		}

		return super.onKeyDown(keyCode, event);
	}

}
