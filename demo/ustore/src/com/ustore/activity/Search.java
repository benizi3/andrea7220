package com.ustore.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.T;
import com.ustore.data.MyData;
import com.ustore.http.Website;
import com.ustore.util.ValidateData;

public class Search extends ListActivity implements OnClickListener, OnGestureListener
{

	// 声明首页分页按钮控件
	private MyData myData = null;
	// private HttpGetRes myHttpGetRes = null;
	/** 声明推荐关键字TextView **/
	private TextView mTextView1 = null, mTextView2 = null, mTextView3 = null, mTextView4 = null, mTextView5 = null, mTextView6 = null,
			mTextView7 = null, mTextView8 = null;
	/** 声明每个关键字的动画效果 **/
	private Animation animation1 = null, animation2 = null, animation3 = null, animation4 = null, animation6 = null, animation7 = null,
			animation8 = null, animation2_1 = null, animation2_2 = null, animation2_3 = null, animation2_4 = null, animation2_5 = null,
			animation2_6 = null, animation2_8 = null;
	/** 声明触屏手势扑捉器 **/
	private GestureDetector gDetector = null;

	/** 声明推荐关键字数组 **/
	private String[] keywords = { "赛车", "塔防", "三国", "找茬", "连连看", "斗地主", "象棋", "动态壁纸", "主题", "手机桌面", "播放器", "小说", "天气", "垃圾清理", "锁屏", "美食",
			"杀毒", "美女", "浏览器", "微信", "QQ游戏", "拍照" };

	private EditText editText = null;
	private ImageView imageView = null;
	private String str = null;
	private ProgressBar myprogressbar = null;// 列表滚动到底部的加载进度条
	/** begin等于0时点击关键字搜索，等于1时不能搜索 **/
	private int begin = 0;

	private static final String TAG_NAME = "搜索";
	TextView title;

	ImageView top1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		getSerachKey();

		initAnimations();
		initAnimations2();
		initViews();
		randomTanslate();
		gDetector = new GestureDetector(this);
		prepareView();

		title = (TextView) findViewById(R.id.t1);
		title.setText(TAG_NAME);
		top1 = (ImageView) findViewById(R.id.top1);

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.ustore.downloadreceiver");
		registerReceiver(downloadReceiver, filter);

	}

	private void getSerachKey()
	{
		final ACache cache = ACache.create();
		String keys = cache.getAsString(Website.SERACH_KEY);

		if (keys != null)
		{
			keywords = keys.split(",");
			L.i(" now keywords =" + keys.toString());
		}

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

		overridePendingTransition(0, 0);
		finish();
	}

	// *** 屏幕手势捕捉器 **//*
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return gDetector.onTouchEvent(event);
	}

	private void initViews()
	{// 实例view
		mTextView1 = (TextView) findViewById(R.id.txt1);
		mTextView2 = (TextView) findViewById(R.id.txt2);
		mTextView3 = (TextView) findViewById(R.id.txt3);
		mTextView4 = (TextView) findViewById(R.id.txt4);
		mTextView5 = (TextView) findViewById(R.id.txt5);
		mTextView6 = (TextView) findViewById(R.id.txt6);
		mTextView7 = (TextView) findViewById(R.id.txt7);
		mTextView8 = (TextView) findViewById(R.id.txt8);
		mTextView1.setOnClickListener(this);
		mTextView2.setOnClickListener(this);
		mTextView3.setOnClickListener(this);
		mTextView4.setOnClickListener(this);
		mTextView5.setOnClickListener(this);
		mTextView6.setOnClickListener(this);
		mTextView7.setOnClickListener(this);
		mTextView8.setOnClickListener(this);
	}

	/** 随机匹配关键字 **/
	private void randomText()
	{

		List<String> list = new ArrayList<String>(Arrays.asList(keywords));
		for (int i = R.id.txt1; i <= R.id.txt8; i++)
		{
			((TextView) findViewById(i)).setText(getKeyword(list));
		}
	}

	/** 随机分配关键字 **/
	private String getKeyword(List<String> list)
	{
		if (list != null && list.size() > 0)
		{
			int num = random.nextInt(list.size());
			String keyword = list.get(num);
			list.remove(num);
			return keyword;
		}
		else
		{
			return "";
		}
	}

	/** 实例动画一 **/
	private void initAnimations()
	{
		animation1 = AnimationUtils.loadAnimation(this, R.anim.anim1);
		animation2 = AnimationUtils.loadAnimation(this, R.anim.anim2);
		animation3 = AnimationUtils.loadAnimation(this, R.anim.anim3);
		animation4 = AnimationUtils.loadAnimation(this, R.anim.anim4);
		animation6 = AnimationUtils.loadAnimation(this, R.anim.anim6);
		animation7 = AnimationUtils.loadAnimation(this, R.anim.anim7);
		animation8 = AnimationUtils.loadAnimation(this, R.anim.anim8);
	}

	/** 实例关键字动画二 **/
	private void initAnimations2()
	{
		animation2_1 = AnimationUtils.loadAnimation(this, R.anim.anim2_1);
		animation2_2 = AnimationUtils.loadAnimation(this, R.anim.anim2_2);
		animation2_3 = AnimationUtils.loadAnimation(this, R.anim.anim2_3);
		animation2_4 = AnimationUtils.loadAnimation(this, R.anim.anim2_4);
		animation2_5 = AnimationUtils.loadAnimation(this, R.anim.anim2_5);
		animation2_6 = AnimationUtils.loadAnimation(this, R.anim.anim2_6);
		animation2_8 = AnimationUtils.loadAnimation(this, R.anim.anim2_8);

	}

	/** 设置应用动画一 **/
	private void startAnimations1()
	{
		mTextView1.startAnimation(animation1);
		mTextView2.startAnimation(animation2);
		mTextView3.startAnimation(animation3);
		mTextView4.startAnimation(animation4);
		mTextView5.startAnimation(animation4);
		mTextView6.startAnimation(animation6);
		mTextView7.startAnimation(animation7);
		mTextView8.startAnimation(animation8);

	}

	/** 设置应用动画二 **/
	private void startAnimations2()
	{
		mTextView1.startAnimation(animation2_1);
		mTextView2.startAnimation(animation2_2);
		mTextView3.startAnimation(animation2_3);
		mTextView4.startAnimation(animation2_4);
		mTextView5.startAnimation(animation2_5);
		mTextView6.startAnimation(animation2_6);
		mTextView8.startAnimation(animation2_8);

	}

	/** 点击屏幕的方法 **/
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	/** 随机调用一个动画 **/
	private void randomTanslate()
	{
		int num = random.nextInt(2);// 返回一个随机的半开区间[0, 2).
		randomText();
		switch (num)
		{
		case 0:
			startAnimations1();
			break;
		case 1:
			startAnimations2();
			break;
		}
	}

	/** 返回一个随机均匀分布中的半开区间一 **/
	private Random random = new Random();// 返回一个随机均匀分布中的半开区间

	/** 滑动屏幕时匹配动画 **/
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{

		randomTanslate();
		return false;
	}

	/** 长按屏幕方法 **/
	public void onLongPress(MotionEvent e)
	{

	}

	/** 屏幕滚动方法 **/
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{

		return false;
	}

	/** 点击屏幕方法 **/
	public void onShowPress(MotionEvent e)
	{}

	/** 触屏时触发的点击效果 **/
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}

	/**
	 * 准备view的内容、控件实例和监听
	 */
	private void prepareView()
	{
		myData = new MyData();
		// 隐藏软键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		editText = (EditText) findViewById(R.id.edit);
		editText.setOnClickListener(this);
		editText.clearFocus();
		imageView = (ImageView) findViewById(R.id.searchbar);
		imageView.clearFocus();
		imageView.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				begin = 0;
				str = editText.getText().toString();
				if (str == null || str.equals(""))
				{
					Toast.makeText(Search.this, "请输入您要查询的内容！", Toast.LENGTH_SHORT).show();

				}
				else
				{
					Pattern pattern = Pattern.compile("\\s*|\t|\r|\n|\\ ");
					Matcher matcher = pattern.matcher(str);
					str = matcher.replaceAll("");
					if (str.length() > 5)
					{
						str = str.substring(0, 5);

					}
					pattern = null;
					matcher = null;

					searchbegin(str);
					Toast.makeText(Search.this, "正在努力为您搜索!", Toast.LENGTH_SHORT).show();

				}
				InputMethodManager imm = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));// 点击搜索按钮后隐藏软键盘输入法
				imm.hideSoftInputFromWindow(Search.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
	}

	/**
	 * 开线程，显示进度条等待联网
	 * 
	 * @param str
	 */
	private void searchbegin(final String str)
	{
		myprogressbar = (ProgressBar) findViewById(R.id.myprogressbar);
		myprogressbar.setVisibility(View.VISIBLE);
		final String urlString = Website.searchurl + str;

		new AbaseHttp().get(urlString, new AjaxCallBack<String>()
		{
			@Override
			public void onSuccess(String t)
			{
				if (ValidateData.validate(t))
				{
				setDate(t);
				ACache.create().put(urlString, t, ACache.TIME_DAY);
				}
				else searchbegin(str);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg)
			{

				Dialog dialog = new AlertDialog.Builder(Search.this).setMessage("网络连接失败 是否重试").setPositiveButton("重试",// 确定按钮
						new android.content.DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								searchbegin(str);
							}
						}).setNegativeButton("退出", new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						Search.this.finish();
					}
				}).create();
				dialog.show();
			}
		});

	}

	private void setDate(String data)
	{
		myData.setNetdates(data);

		getListView().setVisibility(View.VISIBLE);// 显示ListView列表
		myData.sqlAddDate(Search.this, getListView(), "search");

		if (getListView().getCount() == 0) T.ShortToast("没有找到相关应用");

		myprogressbar.setVisibility(View.GONE);
	}

	/**
	 * 按键监听
	 */
	@Override
	public void onClick(View v)
	{

		String tex = null;
		switch (v.getId())
		{

		case R.id.edit:
			editText.setText("");
			getListView().setVisibility(View.GONE);// 隐藏ListView列表
			begin = 0;
			break;
		case R.id.txt1:
			tex = mTextView1.getText().toString();// 得到点击的字符串
			break;
		case R.id.txt2:
			tex = mTextView2.getText().toString();// 得到点击的字符串
			break;
		case R.id.txt3:
			tex = mTextView3.getText().toString();// 得到点击的字符串
			break;
		case R.id.txt4:
			tex = mTextView4.getText().toString();// 得到点击的字符串
			break;
		case R.id.txt5:
			tex = mTextView5.getText().toString();// 得到点击的字符串
			break;
		case R.id.txt6:
			tex = mTextView6.getText().toString();// 得到点击的字符串
			break;
		case R.id.txt7:
			tex = mTextView7.getText().toString();// 得到点击的字符串
			break;
		case R.id.txt8:
			tex = mTextView8.getText().toString();// 得到点击的字符串
			break;
		}
		if (tex != null)
		{
			if (begin == 0)
			{
				editText.setText(tex);
				searchbegin(tex);// 搜索此关键字
				begin = 1;
			}
		}
		overridePendingTransition(0, 0);// 禁用页面跳转的动画效果

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

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unregisterReceiver(downloadReceiver);
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
