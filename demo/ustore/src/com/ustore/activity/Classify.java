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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.ustore.data.MyData;
import com.ustore.http.Website;
import com.ustore.util.ValidateData;

/**
 * app列表
 * 
 * 
 */
public class Classify extends ListActivity
{
	private MyData myData = null;
	// private HttpGetRes mygetRes = null;
	private String id = null;
	private ProgressBar myprogressbar;
	private TextView title;
	private ACache cache = ACache.create();
	private ImageView top1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		prepareView();
		connect();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.ustore.downloadreceiver");
		registerReceiver(downloadReceiver, filter);
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

	/** 准备view要显示的内容和控件，实例各个控件和监听 **/
	private void prepareView()
	{
		setContentView(R.layout.classification);
		myData = new MyData();
		myprogressbar = (ProgressBar) findViewById(R.id.myprogressbar);
		top1 = (ImageView) findViewById(R.id.top1);
		title = (TextView) findViewById(R.id.t1);

		Intent intent = getIntent();
		id = intent.getStringExtra("info");
		title.setText(intent.getStringExtra("tag"));

	}

	/**
	 * 获取列表要显示的数据
	 */
	private void connect()
	{

		myprogressbar.setVisibility(View.VISIBLE);

		// new Thread()
		// {
		// public void run()
		// {
		// Looper.prepare();
		// classinfonnet(); // 连接网络
		// Looper.loop();
		// }

		// public void classinfonnet()
		// {

		String data = null;// 初始化用来加载页面需要是数据
		final String url = Website.classifyurl + id;
		data = cache.getAsString(url);
		if (data != null) setDate(data);

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

				Dialog dialog = new AlertDialog.Builder(Classify.this).setMessage("网络连接失败 是否重试").setPositiveButton("重试",// 确定按钮
						new android.content.DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								connect();
							}
						}).setNegativeButton("退出", new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						Classify.this.finish();
					}
				}).create();
				dialog.show();

			}

		});

	}

	private void setDate(String data)
	{
		myData.setNetdates(data);
		myData.sqlAddDate(Classify.this, getListView(), "class" + id);
		myprogressbar.setVisibility(View.GONE);
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

}
