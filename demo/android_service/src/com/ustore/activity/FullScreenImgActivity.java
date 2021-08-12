package com.ustore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.service.R;
import com.ustore.adapter.FullImgPagerAdapter;

/**
 * 详情全屏图片展示
 * 
 * @author Administrator
 * 
 */
public class FullScreenImgActivity extends Activity
{

	ViewPager viewPager;

	Intent intent;

	LinearLayout linear;

	int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreenimg);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		linear = (LinearLayout) findViewById(R.id.lin);
		intent = getIntent();
		
		if(intent==null) return ;

		String[] image = intent.getStringArrayExtra("image");
		 pos=intent.getIntExtra("pos", 0);

		 FullImgPagerAdapter adapter=new FullImgPagerAdapter(this,image);
		
		 viewPager.setAdapter(adapter);
		
		 viewPager.setCurrentItem(pos);
		 
		
		for (int i = 0; i < image.length; i++)
		{

			ImageView img = new ImageView(this);

			if (i == pos)
			{

				img.setImageResource(R.drawable.pof);
				linear.setTag(img);

			}
			else
			{

				img.setImageResource(R.drawable.pon);
			}

			img.setPadding(0, 0, 10, 0);

			linear.addView(img);
		}

		viewPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int arg0)
			{
				if (linear.getTag() != null)
				{
					((ImageView) (linear.getTag())).setImageResource(R.drawable.pon);
				}

				linear.setTag(linear.getChildAt(arg0));
				((ImageView) linear.getChildAt(arg0)).setImageResource(R.drawable.pof);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub

			}
		});

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
