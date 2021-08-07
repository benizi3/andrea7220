package com.jayqqaa12.abase.core.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Shader.TileMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AbaseFragment extends Fragment implements OnPageChangeListener
{
	private int currIndex = 0;
	private List<Integer> postiion = new ArrayList<Integer>();

	protected View rootView;

	/**
	 * 滑动条 设置 自己的滑动条来 自动生成 滑动动画
	 */
	private View title;

	public View cacheView(int resId, LayoutInflater inflater)
	{
		if (rootView == null) rootView = inflater.inflate(resId, null);
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) parent.removeView(rootView);

		return rootView;
	}

	protected void initTitleWidth(int count, View title)
	{
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int size = (int) (dm.widthPixels / count);

		int i = -1;
		while (count > i++)
		{
			postiion.add(size * i);
		}

		this.title = title;

	}

	@Override
	public void onPageSelected(int pos)
	{
		Animation animation = new TranslateAnimation(postiion.get(currIndex), postiion.get(pos), 0, 0);
		
		if (animation == null) return;

		currIndex = pos;
		animation.setFillAfter(true);
		animation.setDuration(300);
		title.startAnimation(animation);
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

}
