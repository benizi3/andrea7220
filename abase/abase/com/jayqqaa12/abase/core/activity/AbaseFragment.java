package com.jayqqaa12.abase.core.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
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
	protected Abus bus;
	
	protected View rootView;

	
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bus.register(this);
	};
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		bus.unregister(this);
	}

	public View cacheView(int resId, LayoutInflater inflater)
	{
		if (rootView == null) rootView = inflater.inflate(resId, null);
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) parent.removeView(rootView);

		return rootView;
	}


	
	@AfterInject
	protected void afterInject(){
		
	}
 
	
	@AfterViews
	protected void afterView()
	{
		init();
		connect();
	}
	
	protected void init(){
	}
	
	/***
	 * 填充数据 连接网络等
	 */
	protected void connect()
	{
	}



}
