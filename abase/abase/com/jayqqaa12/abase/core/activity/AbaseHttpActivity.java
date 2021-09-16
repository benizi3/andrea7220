package com.jayqqaa12.abase.core.activity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;

@EActivity
public class AbaseHttpActivity extends Activity
{
	
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
	protected void connect()
	{
	}

	

	

}
