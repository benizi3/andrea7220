package com.jayqqaa12.news.ui;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.jayqqaa12.abase.core.activity.AbaseFragment;
import com.jayqqaa12.abase.core.adapter.AbaseFragmentPagerAdapter;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.news.R;

@EFragment(R.layout.fragment)
public class TabFragment extends AbaseFragment 
{
	@ViewById
	ViewPager vp;

	@ViewById(R.id.iv_bottom_line)
	ImageView iv_line;

	private ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Fragment activityfragment = PageFragment.newInstance(R.drawable.xianjian01);
		Fragment groupFragment = PageFragment.newInstance(R.drawable.xianjian02);
		Fragment friendsFragment = PageFragment.newInstance(R.drawable.xianjian03);
		Fragment f4 = PageFragment.newInstance(R.drawable.xianjian03);

		fragmentsList.add(activityfragment);
		fragmentsList.add(groupFragment);
		fragmentsList.add(friendsFragment);
		fragmentsList.add(f4);
		
	}

	@AfterViews
	public void init()
	{
		vp.setAdapter(new AbaseFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
		vp.setOnPageChangeListener(this);
		
		initTitleWidth(fragmentsList.size(), iv_line);
		
	}

	

	@Override
	public void onPageScrollStateChanged(int arg0)
	{}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{}
}
