package com.ustore.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Map;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.Abase;
import com.jayqqaa12.abase.core.AbaseBitmap;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.T;
import com.jayqqaa12.abase.util.network.NetworkUtil;
import com.jayqqaa12.abase.util.sys.SysIntentUtil;
import com.ustore.bean.DownloadListInfo;
import com.ustore.data.DetailsData;
import com.ustore.download.Dao;
import com.ustore.http.HttpGetRes;
import com.ustore.http.Website;
import com.ustore.service.DownloadService;
import com.ustore.util.ValidateData;

public class DownDetails extends Activity implements OnClickListener
{

	private Dao dao = Dao.getInstance(Abase.getContext());

	private DetailsData mydetail = null;
	// 声明一个下载控件
	private ProgressBar downbutton = null;
	// 声明一个返回控件
	private ImageButton backbutton = null;
	// 声明一个id来接收myData传递过来的软件的服务器id号
	private String id = null;
	/* 声明一个list来取出detailsData里面需要适配的下载软件详细信息 */
	private Map<String, String> downlist;
	private String name;
	private String icon;
	private ProgressBar pb;

	private TextView title;
	private ImageView top1;
	private AbaseBitmap bitmap = AbaseBitmap.create();

	/**
	 * 下载的 URL
	 */
	private String downloadUrl;
	/**
	 * 大图容器
	 */
	HorizontalScrollView imgContainer;

	/**
	 * img1 小图 tx1 资费 tx4 评分 tx2 下载量 tx5版本 tx3 时间 tx6大小 tx7 简介 img2 大图
	 */
	ImageView img1;
	TextView tx1, tx2, tx3, tx4, tx5, tx6, tx7;
	/**
	 * state=0下载, state=1暂停 ，state=2启动
	 */
	static int state = -1;
	Drawable drawable1;
	Drawable[] drawable2;
	/**
	 * 灰色背景条，加载完成后显示
	 */
	FrameLayout f2;
	/**
	 * 多图布局
	 */
	LinearLayout lin1;
	/**
	 * 是否已退出详情页
	 */
	private boolean mExit = false;
	/**
	 * 当前详情页apk下载进度
	 */
	private int curProgress = 0;

	/**
	 * 暂停，下载状态文字
	 */
	private TextView pstate;

	private ACache cache = ACache.create();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		L.i("down detail oncreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.downdetails);
		title = (TextView) findViewById(R.id.t1);
		tx1 = (TextView) findViewById(R.id.tx1);
		tx2 = (TextView) findViewById(R.id.tx2);
		tx3 = (TextView) findViewById(R.id.tx3);
		tx4 = (TextView) findViewById(R.id.tx4);
		tx5 = (TextView) findViewById(R.id.tx5);
		tx6 = (TextView) findViewById(R.id.tx6);
		tx7 = (TextView) findViewById(R.id.tx7);
		img1 = (ImageView) findViewById(R.id.img1);
		imgContainer = (HorizontalScrollView) findViewById(R.id.imgcontainer);
		top1 = (ImageView) findViewById(R.id.top1);
		pstate = (TextView) findViewById(R.id.pstate);
		lin1 = (LinearLayout) findViewById(R.id.lin1);
		f2 = (FrameLayout) findViewById(R.id.f2);

		downbutton = (ProgressBar) findViewById(R.id.downbutton);
		backbutton = (ImageButton) findViewById(R.id.backbutton);
		backbutton.setVisibility(View.VISIBLE);
		backbutton.setOnClickListener(this);
		downbutton.setOnClickListener(this);

		init();

		new UpprogressThread().start();
	}

	private void init()
	{
		// 得到点击item时app的实际id
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		downloadUrl = intent.getStringExtra("url");

		L.i("id = " + id + " url = " + downloadUrl);

		if (id == null) return;

		prepareView();

		connet();
	}

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

		// 判断是否下载完成，下载完成显示启动状态

		if (id != null) isComplete();

	}

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
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	/** 准备要显示的view内容，接收传过来的值；实例化监听按钮 **/
	private void prepareView()
	{

		mydetail = new DetailsData();

		// 下载中
		if (DownloadService.downloadingSet.contains(id))
		{
			pstate.setText(R.string.pause);
			state = 0;
		}
		else
		{
			pstate.setText(R.string.download);
			state = 1;
		}

	}

	/**
	 * 判断是否下载完成
	 */
	private void isComplete()
	{
		File apkFile = new File(Website.sd_path + "download", id + ".apk");

		if (apkFile.exists())
		{
			// 下载完成，启动状态
			if (dao.notHasInfors(id))
			{
				state = 2;
				pstate.setText(R.string.boot);
			}
			// 没有下载
			else if (!DownloadService.downloadSet.contains(id))
			{
				DownloadListInfo i = dao.getDownloadListInfo(id);
				if (i != null) pstate.setText("继续下载");
				else pstate.setText(R.string.download);

				state = 1;
			}
			// 下载 中
			else if (DownloadService.downloadingSet.contains(id))
			{
				state = 0;
				pstate.setText(R.string.pause);
			}
		}
		// 没有下载
		else if (!DownloadService.downloadSet.contains(id))
		{
			state = 1;
			pstate.setText(R.string.download);
		}
		// 下载 中
		else if (DownloadService.downloadingSet.contains(id))
		{
			state = 0;
			pstate.setText(R.string.pause);
		}

	}

	/**
	 * 获取要显示的列表的数据
	 */
	private void connet()
	{
		pb = (ProgressBar) findViewById(R.id.myprogressbar2);
		pb.setVisibility(View.VISIBLE);

		getDesc();

	}

	/**
	 * 从网络或缓存 获得 描述信息
	 */
	private void getDesc()
	{
		String data = null;// 初始化用来加载页面需要是数据
		final String url = Website.download_detailsurl + id;

		data = cache.getAsString(url);

		if (data != null) setDate(data);
		else
		{
			new AbaseHttp().get(url, new AjaxCallBack<String>()
			{
				@Override
				public void onSuccess(String t)
				{
					if (ValidateData.validate(t))
					{
						setDate(t);
						ACache.create().put(url, t, ACache.TIME_DAY * 30);
					}
					else connet();
				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{

					Dialog dialog = new AlertDialog.Builder(DownDetails.this).setMessage("网络连接失败 是否重试").setPositiveButton("重试",// 确定按钮
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
									getDesc();
								}
							}).setNegativeButton("退出", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							DownDetails.this.finish();
						}
					}).create();
					dialog.show();
				}
			});

		}

	}

	private void setDate(String data)
	{
		mydetail.setDetailstData(data);
		mydetail.detailsAddDate();

		downlist = mydetail.getDetailsDate();
		Message msg1 = handler.obtainMessage();

		try
		{
			String[] aa = downlist.get("image").split("\\#");
			msg1.arg1 = aa.length;
		} catch (Exception e)
		{
			msg1.arg1 = 0;
		}
		msg1.what = 1;
		handler.sendMessage(msg1);

	}

	/***
	 * 获得图片信息
	 */
	public void getImages()
	{
		L.i("显示图片");

		String icon = downlist.get("icon");
		bitmap.display(img1, icon);
		int length = 0;
		String[] urls = null;

		urls = downlist.get("image").split("\\#");
		length = urls.length;

		f2.setVisibility(View.VISIBLE);

		final String[] image = urls;

		for (int i = 0; i < length; i++)
		{
			ImageView iv = (ImageView) lin1.getChildAt(i);

			final String url = urls[i];
			final int pos = i;

			bitmap.configLoadingImage(R.drawable.bg_bsimg);
			bitmap.configLoadfailImage(R.drawable.bg_bsimg);
			bitmap.display(iv, url);

			iv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent2 = new Intent(DownDetails.this, FullScreenImgActivity.class);
					intent2.putExtra("image", image);
					intent2.putExtra("pos", pos);
					startActivity(intent2);
				}
			});
		}

		pb.setVisibility(View.GONE);

	}

	private myhandler handler = new myhandler(this);

	/** 执行消息并执行相关操作 **/
	private class myhandler extends Handler
	{

		WeakReference<DownDetails> downdetails;

		public myhandler(DownDetails down)
		{

			downdetails = new WeakReference<DownDetails>(down);
		}

		@Override
		public void handleMessage(Message message)
		{

			final DownDetails dow = downdetails.get();
			if (dow == null) { return; }

			switch (message.what)
			{

			// 加载文字完成
			case 1:

				L.i("显示文字");

				dow.downloadgetdate();// 显示列表

				// 大图为两张或以上时
				if (message.arg1 > 1)
				{
					for (int j = 0; j < message.arg1; j++)
					{
						final ImageView img = new ImageView(dow);
						img.setPadding(0, 0, 10, 0);

						LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(new LayoutParams(320, 480));
						img.setLayoutParams(par);
						// img.setImageResource(R.drawable.bg_bsimg);
						dow.lin1.addView(img);

					}

					// 大图为一张或加载不到时，显示一张默认图片
				}
				else
				{
					final ImageView img = new ImageView(dow);
					// img.setImageResource(R.drawable.bg_bsimg);
					FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					params.gravity = Gravity.CENTER;
					dow.lin1.setLayoutParams(params);
					dow.lin1.addView(img);
				}

				dow.imgContainer.setVisibility(View.VISIBLE);

				if (downlist != null) getImages();

				break;

			// 加载图片完成
			case 2:

				break;
			// 更新当前详情页的下载进度
			case 3:
				dow.downbutton.setProgress(dow.curProgress);
				isComplete();
				break;

			// 下载完成，点击启动
			case 4:
				state = 2;
				if (DownloadService.downloadingSet.isEmpty()) Home.isDownLoading = false;

				break;

			case -1:

				if (DownloadService.downloadingSet.isEmpty()) Home.isDownLoading = false;

				state = 1;
				Toast.makeText(dow, "此程序不存在,请选择其它程序下载", Toast.LENGTH_LONG).show();

				break;

			case -2:
				if (DownloadService.downloadingSet.isEmpty())
				{
					Home.isDownLoading = false;
				}
				state = 1;
				Toast.makeText(dow, "sd卡空间不足,下载失败", Toast.LENGTH_LONG).show();
				break;

			case -3:
				if (DownloadService.downloadingSet.isEmpty())
				{
					Home.isDownLoading = false;
				}
				state = 1;
				Toast.makeText(dow, "网络连接错误,请检查网络", Toast.LENGTH_LONG).show();
				break;

			case -4:
				if (DownloadService.downloadingSet.isEmpty())
				{
					Home.isDownLoading = false;
				}
				state = 1;
				Toast.makeText(dow, "下载失败,未检测到SD卡,请安装SD卡!", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	/** 将获取的详细信息显示出来 **/
	private void downloadgetdate()
	{
		try
		{
			if (downlist != null)
			{
				title.setText(downlist.get("name"));
				tx1.setText("资费:" + downlist.get("ischarge"));
				tx2.setText("下载:" + downlist.get("downloadnum"));
				tx3.setText("时间:" + downlist.get("date"));
				tx4.setText("评分:" + downlist.get("totlescore"));
				tx5.setText("版本:" + downlist.get("version"));

				try
				{
					tx6.setText("大小:" + new DecimalFormat("##0.00").format((Float.valueOf(downlist.get("size")) / (1024 * 1024))) + "MB");

				} catch (Exception e)
				{
					tx6.setText("大小:" + downlist.get("size"));
				}

				tx7.setText(downlist.get("information"));

			}
			else
			{
				Toast.makeText(this, "网络连接错误,请检查网络!", Toast.LENGTH_SHORT).show();
				img1.setImageResource(R.drawable.default_icon);
			}

		} catch (Exception e)
		{
			Toast.makeText(this, "网络连接错误,请检查网络!", Toast.LENGTH_SHORT).show();

		}

	}

	public void onClick(View v)
	{

		// 获取用户按下控件的id并执行相应操作
		switch (v.getId())
		{
		case R.id.downbutton:

			name = mydetail.getApkname();
			icon = downlist.get("icon");

			if (top1.getVisibility() == View.GONE) top1.setVisibility(View.VISIBLE);

			switch (state)
			{
			// 下载中 点击暂停
			case 0:
				if (!DownloadService.canotPauseSet.contains(id))
				{

					if (dao.getDownloadListInfo(id) != null) pstate.setText("继续下载");
					else pstate.setText(R.string.download);
					state = 1;
					DownloadService.stopTaskNotStopNotifiction(id);
				}
				else
				{
					T.ShortToast("当前下载 不可暂停");
				}
				break;

			// 暂停状态，点击下载
			case 1:

				Home.isDownLoading = true;
				pstate.setText(R.string.pause);
				state = 0;

				if (Dao.getInstance().getDownloadListInfo(id) == null)
				{
					DownloadListInfo downInfo = new DownloadListInfo(id, name, icon, String.valueOf(0));
					Dao.getInstance(getApplicationContext()).saveDownloadListInfo(downInfo);
				}

				this.startService(DownloadService.getSendIntent(this, id, name, icon, downloadUrl, false));

				break;

			// 启动
			case 2:

				StringBuffer msav = new StringBuffer(Website.sd_path);
				msav.append("download");
				StringBuffer apk_path = new StringBuffer(id);
				apk_path.append(".apk");// 软件安装包的名称
				File f = new File(msav.toString(), apk_path.toString());

				PackageInfo info = null;
				try
				{

					info = getPackageManager().getPackageArchiveInfo(f.toString(), PackageManager.GET_ACTIVITIES);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

				if (info != null)
				{

					ApplicationInfo appInfo = info.applicationInfo;
					String packageName = appInfo.packageName;
					Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
					// 已安装此apk
					if (intent != null)
					{
						DownDetails.this.startActivity(intent);
						// 未安装
					}
					else
					{
						Toast.makeText(DownDetails.this, "请先安装!", Toast.LENGTH_SHORT).show();
						SysIntentUtil.install(this, f);
					}
					// 安装包解析失败
				}
				else
				{
					Toast.makeText(DownDetails.this, "安装包错误,请重新下载", Toast.LENGTH_SHORT).show();
					DownloadService.removeTaskAndDeleteFile(id);

					state = 0;

				}

				break;

			}

			break;
		case R.id.backbutton:
			finish();

			break;
		}

		Home.isover = true;
	}

	/**
	 * 点击返回按钮退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) finish();// 退出程序

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		handler.removeCallbacksAndMessages(null);

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		try
		{

			for (int i = 0; i < lin1.getChildCount(); i++)
			{
				try
				{

					((ImageView) lin1.getChildAt(i)).getDrawable().setCallback(null);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				((ImageView) lin1.getChildAt(i)).setImageDrawable(null);
			}
			lin1.removeAllViews();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		mExit = true;

	}

	/**
	 * 更新进度
	 */
	public class UpprogressThread extends Thread
	{
		public void run()
		{

			L.i("update thread is start  and exit = " + mExit);

			while (!mExit)
			{
				try
				{
					Thread.sleep(800);
					// 获取下载进度。没有则为0
					DownloadListInfo info1 = dao.getDownloadListInfo(id);
					if (info1 != null)
					{
						curProgress = Integer.parseInt(info1.getProgress());
					}
					else
					{
						curProgress = 0;
					}
					handler.sendEmptyMessage(3);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		}
	}

}
