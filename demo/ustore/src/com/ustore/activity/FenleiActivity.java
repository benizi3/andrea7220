package com.ustore.activity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 分类
 */
public class FenleiActivity extends Activity {

	private static final String TAG_NAME = "分类";

	TextView title;
	ImageView top1;

	private LinearLayout rl;

	private Button b1, b2, b3, b4, b5, b6, b7, b8;

	private boolean havaAnim;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		havaAnim = getIntent().getBooleanExtra("havaAnim", true);

		setContentView(R.layout.fenlei);
		title = (TextView) findViewById(R.id.t1);
		title.setText(TAG_NAME);
		top1 = (ImageView) findViewById(R.id.top1);

		rl = (LinearLayout) findViewById(R.id.rl);

		b1 = (Button) findViewById(R.id.f1);
		b2 = (Button) findViewById(R.id.f2);
		b3 = (Button) findViewById(R.id.f3);
		b4 = (Button) findViewById(R.id.f4);
		b5 = (Button) findViewById(R.id.f5);
		b6 = (Button) findViewById(R.id.f6);
		b7 = (Button) findViewById(R.id.f7);
		b8 = (Button) findViewById(R.id.f8);

		if (havaAnim)
			setAnim();

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.ustore.downloadreceiver");
		registerReceiver(downloadReceiver, filter);

	}

	private void setAnim() {



		Animation a1 = null;
		Animation a2 = null;
		Animation a3 = null;
		Animation a4 = null;
		Animation a5 = null;
		Animation a6 = null;
		Animation a7 = null;
		Animation a8 = null;

		int count = new Random().nextInt(3);

		switch (count) {
		case 0:

			a1 = new TranslateAnimation(-1000, 0, 0, 0);
			a2 = new TranslateAnimation(1000, 0, 0, 0);
			a3 = new TranslateAnimation(0, 0, 1000, 0);
			a4 = new TranslateAnimation(0, 0, -1000, 0);
			a5 = new TranslateAnimation(-1000, 0, 0, 0);
			a6 = new TranslateAnimation(0, 0, 1000, 0);
			a7 = new TranslateAnimation(0, 0, 1000, 0);
			a8 = new TranslateAnimation(1000, 0, 0, 0);

			break;
		case 1:
			a1 = new TranslateAnimation(1000, 0, 0, 0);
			a2 = new TranslateAnimation(-1000, 0, 0, 0);
			a3 = new TranslateAnimation(0, 0, -1000, 0);
			a4 = new TranslateAnimation(0, 0, 1000, 0);
			a5 = new TranslateAnimation(1000, 0, 0, 0);
			a6 = new TranslateAnimation(0, 0, -1000, 0);
			a7 = new TranslateAnimation(0, 0, -1000, 0);
			a8 = new TranslateAnimation(-1000, 0, 0, 0);

			break;
		case 2:
			a1 = new TranslateAnimation(0, 0, -1000, 0);
			a2 = new TranslateAnimation(0, 0, 1000, 0);
			a3 = new TranslateAnimation(1000, 0, 0, 0);
			a4 = new TranslateAnimation(-1000, 0, 0, 0);
			a5 = new TranslateAnimation(0, 0, -1000, 0);
			a6 = new TranslateAnimation(1000, 0, 0, 0);
			a7 = new TranslateAnimation(1000, 0, 0, 0);
			a8 = new TranslateAnimation(0, 0, 1000, 0);

			break;
		case 3:

			a1 = new TranslateAnimation(0, 0, 1000, 0);
			a2 = new TranslateAnimation(0, 0, -1000, 0);
			a3 = new TranslateAnimation(-1000, 0, 0, 0);
			a4 = new TranslateAnimation(1000, 0, 0, 0);
			a5 = new TranslateAnimation(0, 0, 1000, 0);
			a6 = new TranslateAnimation(-1000, 0, 0, 0);
			a7 = new TranslateAnimation(-1000, 0, 0, 0);
			a8 = new TranslateAnimation(0, 0, -1000, 0);

			break;

		}

		a1.setDuration(800);
		b1.setAnimation(a1);
		a2.setDuration(1100);
		b2.setAnimation(a2);
		a3.setDuration(800);
		b3.setAnimation(a3);
		a4.setDuration(700);
		b4.setAnimation(a4);
		a5.setDuration(900);
		b5.setAnimation(a5);
		a6.setDuration(700);
		b6.setAnimation(a6);
		a7.setDuration(800);
		b7.setAnimation(a7);
		a8.setDuration(1000);
		b8.setAnimation(a8);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Home.isDownLoading) {
			top1.setVisibility(View.VISIBLE);
		} else {
			top1.setVisibility(View.GONE);
		}
		
		flag =false;
	}

	public void onClickBar(View v) {
		
		

		if(flag ) return ;
		
		flag =true;
		
		

		Intent intent = new Intent();

		Resources res = getResources();

		switch (v.getId()) {

		// 搜索
		case R.id.top2:
			intent.setClass(this, Search.class);
			startActivity(intent);

			break;
			

			// 管理
		case R.id.top3:
			intent.setClass(this, Admin.class);
			startActivity(intent);
			break;

			// 系统工具
		case R.id.f1:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(1));
			intent.putExtra("tag", res.getString(R.string.f1));


			break;

		// 网络社交
		case R.id.f2:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(2));
			intent.putExtra("tag", res.getString(R.string.f2));

			break;

		// 新闻办公
		case R.id.f3:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(3));
			intent.putExtra("tag", res.getString(R.string.f3));

			break;

		// 娱乐视频
		case R.id.f4:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(4));
			intent.putExtra("tag", res.getString(R.string.f4));

			break;

		// 开心游戏
		case R.id.f5:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(5));
			intent.putExtra("tag", res.getString(R.string.f5));

			break;

		// 阅读生活
		case R.id.f6:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(6));
			intent.putExtra("tag", res.getString(R.string.f6));

			break;

		// 地图导航
		case R.id.f7:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(7));
			intent.putExtra("tag", res.getString(R.string.f7));

			break;

		// 旅游购物
		case R.id.f8:
			intent.setClass(this, Classify.class);
			intent.putExtra("info", String.valueOf(8));
			intent.putExtra("tag", res.getString(R.string.f8));

			break;

		}

		startActivity(intent);

		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);


	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals("com.ustore.downloadreceiver")) {

				if (top1.getVisibility() == View.GONE) {
					top1.setVisibility(View.VISIBLE);
				}

			}
		}
	};

	private boolean flag;

	/**
	 * 重写Activity结束时方法
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(downloadReceiver);

	}

	/**
	 * 点击返回按钮退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {// 点击back键
			
			if(flag) return false;
			
			flag = true;
			

			Animation a1 = null;
			Animation a2 = null;
			Animation a3 = null;
			Animation a4 = null;
			Animation a5 = null;
			Animation a6 = null;
			Animation a7 = null;
			Animation a8 = null;

			a1 = new TranslateAnimation(0, -1000, 0, 0);
			a2 = new TranslateAnimation(0, 1000, 0, 0);
			a3 = new TranslateAnimation(0, 0, 0, 1000);
			a4 = new TranslateAnimation(0, 0, 0, -1000);
			a5 = new TranslateAnimation(0, -1000, 0, 0);
			a6 = new TranslateAnimation(0, 0, 0, 1000);
			a7 = new TranslateAnimation(0, 0, 0, 1000);
			a8 = new TranslateAnimation(0, 1000, 0, 0);

			a1.setFillAfter(true);
			a2.setFillAfter(true);
			a3.setFillAfter(true);
			a4.setFillAfter(true);
			a5.setFillAfter(true);
			a6.setFillAfter(true);
			a7.setFillAfter(true);
			a8.setFillAfter(true);

		

			a1.setDuration(1111);
			a2.setDuration(999);
			a3.setDuration(1055);
			a4.setDuration(888);
			a5.setDuration(799);
			a6.setDuration(1000);
			a7.setDuration(922);
			a8.setDuration(888);
			
			b1.startAnimation(a1);
			b2.startAnimation(a2);
			b3.startAnimation(a3);
			b4.startAnimation(a4);
			b5.startAnimation(a5);
			b6.startAnimation(a6);
			b7.startAnimation(a7);
			b8.startAnimation(a8);
			
			new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
					finish();
				}
			}, 333);
			
			

		}
		return false;
	}


}
