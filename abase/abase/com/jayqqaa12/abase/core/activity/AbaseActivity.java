package com.jayqqaa12.abase.core.activity;

import com.lidroid.xutils.ViewUtils;

import android.app.Activity;
import android.os.Bundle;

public class AbaseActivity extends Activity
{
@Override
protected void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
	
	ViewUtils.inject(this);
}
}
