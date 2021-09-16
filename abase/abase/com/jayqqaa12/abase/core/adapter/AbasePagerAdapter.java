package com.jayqqaa12.abase.core.adapter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class AbasePagerAdapter extends PagerAdapter
{
	private Map<Integer, View> views = new HashMap<Integer, View>();

	public AbasePagerAdapter(Context context, Map<Integer, View> views)
	{
		this.views = views;
	}

	public AbasePagerAdapter(Context context, int... layoutRes)
	{
		int i = 0;
		for (int layout : layoutRes)
			this.views.put(i++, View.inflate(context, layout, null));

	}

	public AbasePagerAdapter(Context context, View... views)
	{
		int i = 0;
		for (View v : views)
			this.views.put(i++, v);
	}

	@Override
	public void destroyItem(View container, int position, Object object)
	{
		((ViewPager) container).removeView(views.get(position));

	}

	@Override
	public Object instantiateItem(View container, int position)
	{

		if (views.get(position).getParent() == null) ((ViewPager) container).addView(views.get(position), 0);
		else
		{
			((ViewGroup) views.get(position).getParent()).removeView(views.get(position));
			((ViewPager) container).addView(views.get(position), 0);
		}

		return views.get(position);
	}

	@Override
	public int getCount()
	{
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View v, Object o)
	{
		return v == o;
	}

}
