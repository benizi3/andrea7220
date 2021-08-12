package com.ustore.activity;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.util.network.NetworkUtil;
import com.ustore.adapter.MySimpleAdapter;
import com.ustore.data.MyData;
import com.ustore.http.HttpGetRes;
import com.ustore.util.ValidateData;

/**
 * 专题
 * 
 */
public class Subject extends ListActivity
{

	private MyData myData = null;
	// private HttpGetRes myHttpGetRes = null;
	private ImageButton classification = null;
	private ImageButton search = null;
	private ProgressBar myprogressbar = null;// 列表滚动到底部的加载进度条
	private String subjectname = null;
	/** 记录点击返回back键时间 **/
	private long mExitTime;

	private static final String TAG_NAME = "专题";
	TextView title;
	ImageView top1;

	private ACache cache = ACache.create();
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject);
		title = (TextView) findViewById(R.id.t1);

		String str = getIntent().getStringExtra("title");

		if (str == null)
		{
			str = "装机必备";
		}

		title.setText(str);

		top1 = (ImageView) findViewById(R.id.top1);
		prepareView();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.ustore.downloadreceiver");
		registerReceiver(downloadReceiver, filter);
		connect();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
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

	/**
	 * 准备view的内容、实例监听控件
	 */
	private void prepareView()
	{
		myData = new MyData();

		Intent mintent = getIntent();
		subjectname = mintent.getStringExtra("name");
		url = mintent.getStringExtra("url");

	}

	/** 开启线程连接服务器获取数据 **/
	private void connect()
	{

		myprogressbar = (ProgressBar) findViewById(R.id.myprogressbar);
		myprogressbar.setVisibility(View.VISIBLE);

		String string = null;// 初始化用来加载页面需要是数据

		/** 判断sd卡是否有数据,有直接读取，否则连接网络 **/
		string = cache.getAsString(url);
		if (string != null) setDate(string);

		new AbaseHttp().get(url, new AjaxCallBack<String>()
		{
			@Override
			public void onSuccess(String t)
			{
				if (ValidateData.validate(t))
				{
					setDate(t);
					ACache.create().put(url, t, ACache.TIME_DAY * 3);
				}
				else connect();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{

				Dialog dialog = new AlertDialog.Builder(Subject.this).setMessage("网络连接失败 是否重试").setPositiveButton("重试",// 确定按钮
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								connect();
							}
						}).setNegativeButton("退出", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						Subject.this.finish();
					}
				}).create();
				dialog.show();

			}
		});

	}

	public void setDate(String date)
	{

		myData.setNetdates(date);

		myData.sqlAddDate(Subject.this, getListView(), subjectname);
		subjectname = null;

		myprogressbar.setVisibility(View.GONE);
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{

			if (intent.getAction().equals("com.ustore.downloadreceiver"))
			{

				if (top1.getVisibility() == View.GONE)
				{
					top1.setVisibility(View.VISIBLE);
				}

			}
		}
	};

	/**
	 * 重写Activity结束时方法
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(downloadReceiver);
		MySimpleAdapter.flag.clear();
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
