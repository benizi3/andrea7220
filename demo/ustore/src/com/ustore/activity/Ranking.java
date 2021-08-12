package com.ustore.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
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

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.util.common.Validate;
import com.ustore.data.MyData;
import com.ustore.http.Website;
import com.ustore.util.ValidateData;

@SuppressLint("HandlerLeak")
public class Ranking extends Activity
{
	private static final String TAG_NAME = "排行";

	private ProgressBar myprogressbar;
	public static String activity_name;

	TextView title;
	private ViewPager mPager;
	private List<View> Views;
	private ImageView cursor;
	private TextView t1, t2;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private ACache cache = ACache.create();
	ListView list1, list2;
	ImageView top1;
	Resources res = null;

	private MyData appdata = new MyData();
	private MyData gamedata = new MyData();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);
		prepareView();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.ustore.downloadreceiver");
		registerReceiver(downloadReceiver, filter);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (Home.isDownLoading) top1.setVisibility(View.VISIBLE);
		else top1.setVisibility(View.GONE);

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

	private void prepareView()
	{

		title = (TextView) findViewById(R.id.t1);
		title.setText(TAG_NAME);
		top1 = (ImageView) findViewById(R.id.top1);
		res = getResources();

		myprogressbar = (ProgressBar) findViewById(R.id.prog01);
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(res, R.drawable.line).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 2 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t2.setTextColor(res.getColor(R.color.disable));

		mPager = (ViewPager) findViewById(R.id.vPager);
		Views = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		Views.add(mInflater.inflate(R.layout.lay1, null));
		Views.add(mInflater.inflate(R.layout.lay1, null));
		mPager.setAdapter(new MyPagerAdapter(Views));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());

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
				list1 = (ListView) arg0.findViewById(R.id.list1);
				connect("apprank");

			}
			else if (arg1 == 1)
			{
				list2 = (ListView) arg0.findViewById(R.id.list1);
				connect("gamerank");

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

	/**
	 */
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
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{}

		public void onPageScrollStateChanged(int arg0)
		{}
	}

	private void connect(final String name)
	{
		myprogressbar.setVisibility(View.VISIBLE);
		
		String string = null;
		String url = null;

		if (name.equals("gamerank")) string = cache.getAsString(Website.gamerank_url);
		else if (name.equals("apprank")) string = cache.getAsString(Website.apprank_url);

		if (Validate.notEmpty(string))
		{
			setNetDate(name, string);
			return;
		}

		if (name.equals("gamerank")) url = Website.gamerank_url;
		else url = Website.apprank_url;
		final String u = url;

		new AbaseHttp().get(url, new AjaxCallBack<String>()
		{
			@Override
			public void onSuccess(String t)
			{
				if (ValidateData.validate(t))
				{
					setNetDate(name, t);
					ACache.create().put(u, t, ACache.TIME_DAY);
				}
				else connect(name);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{

				Dialog dialog = new AlertDialog.Builder(Ranking.this).setMessage("网络连接失败 是否重试").setPositiveButton("重试",// 确定按钮
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								connect(name);
							}
						}).setNegativeButton("退出", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						Ranking.this.finish();
					}
				}).create();
				dialog.show();

			}
		});
	}

	private void setNetDate(final String name, String string)
	{
		if (name.equals("gamerank"))
		{
			gamedata.setNetdates(string);
			gamedata.sqlAddDate(Ranking.this, list2, "gamerank");
		}
		else if (name.equals("apprank"))
		{
			appdata.setNetdates(string);
			appdata.sqlAddDate(Ranking.this, list1, "apprank");
		}
		myprogressbar.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(downloadReceiver);
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
