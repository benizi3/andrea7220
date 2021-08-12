package com.ustore.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.Validate;
import com.jayqqaa12.abase.util.network.ApnUtil;
import com.jayqqaa12.abase.util.network.DownLoadUtil;
import com.jayqqaa12.abase.util.network.NetworkUtil;
import com.jayqqaa12.abase.util.phone.TelUtil;
import com.jayqqaa12.abase.util.ui.DialogUtil;
import com.ustore.adapter.MySimpleAdapter;
import com.ustore.adapter.SubjectImageAdapter;
import com.ustore.bean.HomeGuideBean;
import com.ustore.data.MyData;
import com.ustore.http.HttpGetRes;
import com.ustore.http.Website;
import com.ustore.util.App;
import com.ustore.util.ValidateData;
import com.ustore.view.Gallery1;

public class Home extends Activity
{

	/** 页面释放开关，当页面关闭后终止当前页面的线程 **/
	public static boolean isover = false;
	public static boolean isDownLoading = false;

	private Gallery1 gallery = null;
	private int num;// 专题图片个数

	/** 记录点击返回back键时间 **/
	private long mExitTime;

	private List<HomeGuideBean> mylist = new ArrayList<HomeGuideBean>();

	LinearLayout dots;
	ImageView top1;
	SubjectImageAdapter subad;

	private Button b1, b2, b3, b4, b5;
	private Animation a, a1, a2, a3, a4, a5;
	private boolean redit;

	private ACache cache = ACache.create();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		prepareView();

		conect();
		setAnim();

	}

	private void setAnim()
	{

		Animation[] set = new Animation[] { new TranslateAnimation(-1000, 0, 0, 0), new TranslateAnimation(1000, 0, 0, 0),
				new TranslateAnimation(0, 0, 1000, 0), new TranslateAnimation(0, 0, -1000, 0) };

		Animation a = set[new Random().nextInt(3)];
		a.setDuration(1000);
		gallery.setAnimation(a);
		dots.setAnimation(a);

		Animation a1 = set[new Random(System.currentTimeMillis()).nextInt(3)];
		Animation a5 = set[new Random(System.currentTimeMillis() + 11).nextInt(3)];
		while (a1 == a5)
			a5 = set[new Random(System.currentTimeMillis() + 222).nextInt(3)];

		Animation a2 = set[new Random(System.currentTimeMillis() + 111).nextInt(3)];
		Animation a3 = set[new Random(System.currentTimeMillis() + 1111).nextInt(3)];
		Animation a4 = set[new Random(System.currentTimeMillis() + 11111).nextInt(3)];
		while (a1 == a2)
			a2 = set[new Random(System.currentTimeMillis() + 333).nextInt(3)];
		while (a2 == a3)
			a3 = set[new Random(System.currentTimeMillis() + 22).nextInt(3)];
		while (a3 == a4)
			a4 = set[new Random(System.currentTimeMillis() + 1).nextInt(3)];

		a1.setDuration(new Random(System.currentTimeMillis()).nextInt(500) + 900);
		b1.setAnimation(a1);

		a5.setDuration(new Random(System.currentTimeMillis() + 111).nextInt(500) + 1111);
		b5.setAnimation(a5);

		a2.setDuration(new Random(System.currentTimeMillis() + 112).nextInt(500) + 700);
		b2.setAnimation(a2);

		a3.setDuration(new Random(System.currentTimeMillis() + 1122).nextInt(500) + 600);
		b3.setAnimation(a3);

		a4.setDuration(new Random(System.currentTimeMillis() + 11222).nextInt(500) + 1000);
		b4.setAnimation(a4);

	}

	private boolean flag;

	/**
	 * 按钮点击事件
	 * 
	 * @param v
	 */
	public void onClickBar(View v)
	{

		if (flag) return;

		flag = true;

		Animation[] set = new Animation[] { new TranslateAnimation(0, -1000, 0, 0), new TranslateAnimation(0, 1000, 0, 0),
				new TranslateAnimation(0, 0, 0, 1000), new TranslateAnimation(0, 0, 0, -1000) };

		a = set[new Random().nextInt(3)];

		a1 = set[new Random(System.currentTimeMillis()).nextInt(3)];
		a5 = set[new Random(System.currentTimeMillis() + 11).nextInt(3)];

		while (a1 == a5)
			a5 = set[new Random(System.currentTimeMillis() + 222).nextInt(3)];

		a2 = set[new Random(System.currentTimeMillis() + 111).nextInt(3)];
		a3 = set[new Random(System.currentTimeMillis() + 1111).nextInt(3)];
		a4 = set[new Random(System.currentTimeMillis() + 11111).nextInt(3)];
		while (a1 == a2)
			a2 = set[new Random(System.currentTimeMillis() + 333).nextInt(3)];
		while (a2 == a3)
			a3 = set[new Random(System.currentTimeMillis() + 22).nextInt(3)];
		while (a3 == a4)
			a4 = set[new Random(System.currentTimeMillis() + 1).nextInt(3)];

		a.setDuration(444);
		a1.setDuration(777);
		a2.setDuration(911);
		a3.setDuration(844);
		a4.setDuration(555);
		a5.setDuration(888);

		a.setFillAfter(true);
		a1.setFillAfter(true);
		a2.setFillAfter(true);
		a3.setFillAfter(true);
		a4.setFillAfter(true);
		a5.setFillAfter(true);

		final View view = v;

		gallery.startAnimation(a);
		dots.startAnimation(a);

		new Timer().schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				intoIntent(view);
				redit = true;
			}
		}, 333);

		b1.startAnimation(a1);
		b2.startAnimation(a2);
		b3.startAnimation(a3);
		b4.startAnimation(a4);
		b5.startAnimation(a5);

	}

	private void intoIntent(View v)
	{

		Intent intent = new Intent();

		switch (v.getId())
		{

		// 分类
		case R.id.b1:

			intent.setClass(this, FenleiActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);

			break;

		// 专题
		case R.id.b2:

			intent.setClass(this, ZhuanTiActivity.class);
			startActivity(intent);

			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			break;

		// 排行
		case R.id.b3:

			intent.setClass(this, Ranking.class);
			startActivity(intent);

			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			break;

		// 管理
		case R.id.b4:

			intent.setClass(this, Admin.class);
			startActivity(intent);

			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			break;

		// 装机必备
		case R.id.b5:

			Intent intent2 = new Intent();
			intent2.setClass(this, Subject.class);

			intent2.putExtra("url", Website.subject_url + "5");// 将地址传递到专题类中
			intent2.putExtra("name", "subject5");// 将文件名传递到专题类中
			startActivity(intent2);// 跳转到专题类

			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			break;

		// 搜索
		case R.id.top2:

			intent.setClass(this, Search.class);
			startActivity(intent);

			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			break;

		// 管理
		case R.id.top3:

			intent.setClass(this, Admin.class);
			startActivity(intent);

			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			break;
		}

	}

	/** 准备view显示，实例各个控件 **/
	public void prepareView()
	{
		dots = (LinearLayout) findViewById(R.id.dots);

		top1 = (ImageView) findViewById(R.id.top1);
		gallery = (Gallery1) findViewById(R.id.g);// 实例化Gallery控件

		subad = new SubjectImageAdapter(this, mylist);
		gallery.setAdapter(subad);// 添加到适配

		gallery.setHorizontalFadingEdgeEnabled(false);
		gallery.setSelection(5000);// 从第五千个开始显示

		b1 = (Button) findViewById(R.id.b1);
		b2 = (Button) findViewById(R.id.b2);
		b3 = (Button) findViewById(R.id.b3);
		b4 = (Button) findViewById(R.id.b4);
		b5 = (Button) findViewById(R.id.b5);
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

		if (redit)
		{
			startActivity(new Intent(this, Home.class));
			overridePendingTransition(0, 0);
			finish();
		}

		flag = false;

	}

	public void setListner()
	{

		num = mylist.size();

		// num = 5;

		gallery.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				if (num == 0)
				{// 如果联网失败无法获取图片个数，发出提示信息，并中断下面的操作
					Toast.makeText(Home.this, "网络错误,请连接网络!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (position >= num)
				{
					position = position % num;// 取余得到第几张图片位置
				}
				try
				{

					HomeGuideBean homeGuide = mylist.get(position);
					Intent intent = new Intent();
					/**
					 * type =1链接 2专题 3详情
					 */
					switch (homeGuide.type)
					{

					case 1:
						intent.setAction(Intent.ACTION_VIEW);
						Uri content_url = Uri.parse(homeGuide.data);
						intent.setData(content_url);

						startActivity(intent);

						break;

					case 2:

						String subjectUrl = Website.subject_url + mylist.get(position).data;// 专题链接地址
						String name = "subject" + mylist.get(position).data;// 存储到内存卡的文件名

						intent.setClass(Home.this, Subject.class);
						intent.putExtra("url", subjectUrl);// 将地址传递到专题类中
						intent.putExtra("name", name);// 将文件名传递到专题类中

						intent.putExtra("title", homeGuide.title);
						intent.putExtra("description", homeGuide.desc);

						startActivity(intent);// 跳转到专题类
						break;

					case 3:

						intent.putExtra("id", homeGuide.data);
						intent.putExtra("url", homeGuide.url);
						intent.setClass(Home.this, DownDetails.class);
						startActivity(intent);

						break;

					}

					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

		// add

		for (int i = 0; i < num; i++)
		{
			ImageView img = new ImageView(this);

			if (i == 0) img.setImageResource(R.drawable.pf);
			else img.setImageResource(R.drawable.p);

			img.setPadding(0, 0, 10, 0);

			if (dots.getChildCount() < num) dots.addView(img);
		}

		dots.setTag(dots.getChildAt(0));

		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position >= num) position = position % num;// 取余得到第几张图片位置

				if (dots.getTag() != null) ((ImageView) (dots.getTag())).setImageResource(R.drawable.p);

				dots.setTag(dots.getChildAt(position));
				((ImageView) dots.getChildAt(position)).setImageResource(R.drawable.pf);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{}
		});

		ghandler.postDelayed(runna, 5000);// 每5秒执行一次runnable.
	}

	public Handler ghandler = new Handler();
	public Runnable runna = new Runnable()
	{// 定时器开始，每五秒向右翻转一张专题图片
		public void run()
		{
			gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);// 向右滑动
			Ghandler.postDelayed(this, 5000);

		}
	};

	/**
	 * 新起线程获取列表中要显示的数据 获得 导航 图片
	 */
	public void conect()
	{
		String c = cache.getAsString(Website.INDEX_GUIDE);
		mylist.clear();
		if (c != null) setData(c);

		else
		{
			new AbaseHttp().get(Website.INDEX_GUIDE, new AjaxCallBack<String>()
			{
				@Override
				public void onSuccess(String t)
				{
					if (ValidateData.validate(t))
					{

						setData(t);
						ACache.create().put(Website.INDEX_GUIDE, t, ACache.TIME_DAY);
					}
					else conect();
				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{

					Dialog dialog = new AlertDialog.Builder(Home.this).setMessage("网络连接失败 是否重试").setPositiveButton("重试",// 确定按钮
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
									conect();
								}
							}).setNegativeButton("退出", new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							Home.this.finish();
						}
					}).create();
					dialog.show();

				}
			});

		}

	}

	private void setData(String c)
	{
		String[] c2 = c.split("\\}\\{");
		final String[] c3 = (c2[1].substring(0, c2[1].length() - 1)).split("\\|");

		for (final String cc3 : c3)
		{
			String c4 = null;
			String url = null;
			try
			{
			HomeGuideBean guide = new HomeGuideBean();
		
				c4 = cc3.split("\\^")[0];
				url = Website.INDEX_GUIDE_IMG + c4;
				// 获取图片
				guide.imgurl = url;
				guide.type = Integer.valueOf(cc3.split("\\^")[1]);
				guide.data = cc3.split("\\^")[2];

				if (guide.type == 2)
				{
					guide.title = cc3.split("\\^")[3];
					guide.desc = cc3.split("\\^")[4];
				}

				if (guide.type == 3)
				{
					guide.url = cc3.split("\\^")[3];
				}

				mylist.add(guide);
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		setListner();

	}

	/**
	 * 点击返回按钮退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{// 点击back键
			if ((System.currentTimeMillis() - mExitTime) > 2000)
			{
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			}
			else
			{
				finish();
				Ghandler.removeCallbacks(runnable);// 停止专题图片的计时器
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				try
				{
					MySimpleAdapter.flag.clear();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public Handler Ghandler = new Handler();
	public Runnable runnable = new Runnable()
	{// 定时器开始，每五秒向右翻转一张专题图片
		public void run()
		{
			gallery.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);// 向右滑动
			Ghandler.postDelayed(this, 5000);

		}
	};
}
