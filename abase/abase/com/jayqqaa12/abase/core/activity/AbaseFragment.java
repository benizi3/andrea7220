package com.jayqqaa12.abase.core.activity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jayqqaa12.abase.core.Abus;

@EFragment
public class AbaseFragment extends Fragment  
{
	
	@Bean
	Abus bus;
	
	protected View rootView;


	public View cacheView(int resId, LayoutInflater inflater)
	{
		if (rootView == null) rootView = inflater.inflate(resId, null);
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) parent.removeView(rootView);

		return rootView;
	}




}
