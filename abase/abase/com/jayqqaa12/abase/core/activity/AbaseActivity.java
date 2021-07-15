package com.jayqqaa12.abase.core.activity;

import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.jayqqaa12.abase.core.Abase;
import com.jayqqaa12.abase.core.listener.Listener;
import com.jayqqaa12.abase.core.listener.OnLoadStatus;
import com.jayqqaa12.abase.util.common.T;

/**
 * activiy 的ioc
 * 
 * @author jayqqaa12. 
 * 
 */
public class AbaseActivity extends Activity implements Listener,OnLoadStatus
{

	

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Abase.init(this);

	}

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		Abase.initViewForReflect(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params)
	{
		super.setContentView(view, params);
		Abase.initViewForReflect(this);
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		Abase.initViewForReflect(this);
	}


	protected void showToast(String msg)
	{
		T.ShortToast(msg);
	}


	// ////////////////////////////////////Listener//////////////////////////////////////////////////
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{

	}

	@Override
	public void afterTextChanged(Editable s)
	{

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{

	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
	{
		return false;
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	@Override
	public void onPageSelected(int arg0)
	{

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
	}

	@Override
	public void onClick(View v)
	{
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		return false;
	}

	@Override
	public boolean onLongClick(View v)
	{
		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Abase.cleanCache();
		
		
	}

	
	/***
	 * 这个 接口 配合 abaseHttp  onSuccess 方法使用 可以用户 回调 Activity 的函数 
	 */

	@Override
	public void onLoadStatus(int what,Object obj)
	{
		// TODO Auto-generated method stub
		
	}

}
