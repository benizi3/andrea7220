package com.jayqqaa12.abase.core.activity;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

public class AbaseFragment extends Fragment
{

	protected View rootView;

	public View cacheView(int resId, LayoutInflater inflater)
	{
		if (rootView == null) rootView = inflater.inflate(resId, null);
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) parent.removeView(rootView);

		ViewUtils.inject(this, rootView);
		return rootView;
	}

}
