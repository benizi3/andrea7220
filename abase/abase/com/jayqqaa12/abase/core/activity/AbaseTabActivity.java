package com.jayqqaa12.abase.core.activity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jayqqaa12.abase.annotation.view.FindView;
import com.jayqqaa12.abase.core.Abase;
import com.jayqqaa12.abase.core.AbaseApp;
import com.jayqqaa12.abase.core.listener.Listener;
import com.lidroid.xutils.ViewUtils;

/**
 * 注意 这里的	TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
 * 只能 通过 这种 方式  不能 ioc
 * 
 * @author jayqqaa12
 * @date 2013-4-15
 */
public class AbaseTabActivity extends TabActivity implements  OnTabChangeListener
{


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		ViewUtils.inject(this);
	}



	/**
	 * 使用 getTabHost() 方式时使用 ， 自定义 风格时候用的 快捷方式 同时 实现 IOC 可实现 文字的 自动 装配
	 * 
	 * @param contentLayoutId
	 *            总的activity 的布局 文件 也就是TabContent
	 * @param tabWidgetLayoutId
	 *            自定义的 TabWight layout的 id
	 * @param tabTextViewLabel
	 *            自定义 Tabwight 的TextView 文字
	 * @param tabTextViewId
	 *            自定义 Tabwight 的TextView id
	 * @param lables
	 *            每个 tab的 标签
	 * @param contentIds
	 *            每个content 的id
	 */
	protected void initMyTab(int contentLayoutId, int tabWidgetLayoutId, String[] tabTextViewLabel, int tabTextViewId, String[] lables,
			int[] contentIds)
	{
		TabHost tabHost = getTabHost();
		LayoutInflater.from(this).inflate(contentLayoutId, tabHost.getTabContentView(), true);

		int length = lables.length;
		int i = -1;

		while (++i < length)
		{
			View tab = (View) LayoutInflater.from(this).inflate(tabWidgetLayoutId, null);
			View view = tab.findViewById(tabTextViewId);
			if (view instanceof TextView) ((TextView) view).setText(tabTextViewLabel[i]);

			tabHost.addTab(tabHost.newTabSpec(lables[i]).setIndicator(tab).setContent(contentIds[i]));

		}
		init(tabHost);

	}

	/**
	 * 使用 getTabHost() 方式时使用 ， 自定义 风格时候用的 快捷方式 同时 实现 IOC 可实现 文字的 自动 装配
	 * 
	 * @param contentLayoutId
	 *            总的activity 的布局 文件 也就是TabContent
	 * @param tabWidgetLayoutId
	 *            自定义的 TabWight layout的 id
	 * @param tabTextViewLabel
	 *            自定义 Tabwight 的TextView 文字
	 * @param tabTextViewId
	 *            自定义 Tabwight 的TextView id
	 * @param lables
	 *            每个 tab的 标签
	 * @param contentIds
	 *            每个content 的id
	 */
	protected void initMyTab(int contentLayoutId, int tabWidgetLayoutId, String[] tabTextViewLabel, int tabTextViewId, String[] lables,
			Intent[] contentIds)
	{
		TabHost tabHost = getTabHost();
		LayoutInflater.from(this).inflate(contentLayoutId, tabHost.getTabContentView(), true);

		int length = lables.length;
		int i = -1;

		while (++i < length)
		{
			View tab = (View) LayoutInflater.from(this).inflate(tabWidgetLayoutId, null);
			View view = tab.findViewById(tabTextViewId);
			if (view instanceof TextView) ((TextView) view).setText(tabTextViewLabel[i]);

			tabHost.addTab(tabHost.newTabSpec(lables[i]).setIndicator(tab).setContent(contentIds[i]));

		}
		init(tabHost);

	}

	/**
	 * 使用 getTabHost() 方式时使用 ， 自定义 风格时候用的 快捷方式 同时 实现 IOC 可实现 文字的 自动 装配
	 * 
	 * @param tabWidgetLayoutId
	 *            自定义的 TabWight layout的 id
	 * @param tabTextViewLabel
	 *            自定义 Tabwight 的TextView 文字
	 * @param tabTextViewId
	 *            自定义 Tabwight 的TextView id
	 * @param lables
	 *            每个 tab的 标签
	 * @param contentIds
	 *            每个content 的id
	 */
	protected void initMyTab(int tabWidgetLayoutId, String[] tabTextViewLabel, int tabTextViewId, String[] lables, Intent[] contentIds)
	{
		TabHost tabHost = getTabHost();

		int length = lables.length;
		int i = -1;

		while (++i < length)
		{
			View tab = (View) LayoutInflater.from(this).inflate(tabWidgetLayoutId, null);
			View view = tab.findViewById(tabTextViewId);
			if (view instanceof TextView) ((TextView) view).setText(tabTextViewLabel[i]);

			tabHost.addTab(tabHost.newTabSpec(lables[i]).setIndicator(tab).setContent(contentIds[i]));

		}
		init(tabHost);

	}

	/**
	 * 使用 getTabHost() 方式时使用 ， 自定义 风格时候用的 快捷方式 同时 实现 IOC 可实现 所有类型的自动 装配
	 * 
	 * @param contentLayoutId
	 * @param tabWidgetLayoutId
	 * @param tabWidgetResIds
	 *   子控件Id的 资源 Id  应该是 {{icon,text}...}这样的形式 和 tabWidgetIds 顺序 相对应;
	 * @param tabWidgetIds
	 * @param lables
	 * @param contentIds
	 */
	protected void initMyTab(int contentLayoutId, int tabWidgetLayoutId, int[][] tabWidgetResIds, int[] tabWidgetIds, String[] lables,
			int[] contentIds)
	{
		TabHost tabHost = getTabHost();
		LayoutInflater.from(this).inflate(contentLayoutId, tabHost.getTabContentView(), true);

		int length = lables.length;
		int i = -1;

		while (++i < length)
		{
			View tab = (View) LayoutInflater.from(this).inflate(tabWidgetLayoutId, null);

			int j = -1;

			while (++j < tabWidgetIds.length)
			{
				View view = tab.findViewById(tabWidgetIds[j]);
				if (view instanceof TextView) ((TextView) view).setText(tabWidgetResIds[i][j]);
				if (view instanceof ImageView) view.setBackgroundResource(tabWidgetResIds[i][j]);

			}

			tabHost.addTab(tabHost.newTabSpec(lables[i]).setIndicator(tab).setContent(contentIds[i]));

		}
		init(tabHost);

	}

	/**
	 * 没有使用 getTabHost() 方式时使用 ， 自定义 风格Tab时候用的 快捷方式 同时 实现 IOC 可实现 所有类型的自动 装配
	 * 
	 * @param tabHost
	 * @param tabWidgetLayoutId
	 * @param tabWidgetResIds
	 *   子控件Id的 资源 Id  应该是 {{icon,text}...}这样的形式 和 tabWidgetIds 顺序 相对应;
	 * @param tabWidgetIds
	 *            子控件Id
	 * @param lables
	 *            标签
	 * @param contentIds
	 *            内容
	 */
	protected void initMyTab(TabHost tabHost, int tabWidgetLayoutId, int[][] tabWidgetResIds, int[] tabWidgetIds, String[] lables, int[] contentIds)
	{

		int length = lables.length;
		int i = -1;

		while (++i < length)
		{
			View tab = (View) LayoutInflater.from(this).inflate(tabWidgetLayoutId, null);

			int j = -1;

			while (++j < tabWidgetIds.length)
			{
				View view = tab.findViewById(tabWidgetIds[j]);
				if (view instanceof TextView) ((TextView) view).setText(tabWidgetResIds[i][j]);
				if (view instanceof ImageView) ((ImageView) view).setImageResource(tabWidgetResIds[i][j]);
			}

			tabHost.addTab(tabHost.newTabSpec(lables[i]).setIndicator(tab).setContent(contentIds[i]));

		}

		init(tabHost);
	}
	


	/**
	 * 
	 * 获得 自定义的 tabhost  默认 使用 android.R.id.tabhost
	 * @return
	 */
	protected TabHost getMyTabHost()
	{
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		return tabHost;
	}

	
	
	

	/**
	 * 没有使用 getTabHost() 方式时使用 ， 自定义 风格Tab时候用的 快捷方式 同时 实现 IOC 可实现 所有类型的自动 装配
	 * 
	 * @param tabHost
	 * @param tabWidgetLayoutId 
	 * @param tabWidgetResIds
	 *            子控件Id的 资源 Id  应该是 {{icon,text}...}这样的形式 和 tabWidgetIds 顺序 相对应;
	 * @param tabWidgetIds
	 *            子控件Id
	 * @param lables
	 *            标签
	 * @param Intents
	 *            内容
	 */
	protected void initMyTab(TabHost tabHost, int tabWidgetLayoutId, int[][] tabWidgetResIds, int[] tabWidgetIds, String[] lables, Intent[] intents)
	{

		int length = lables.length;
		int i = -1;

		while (++i < length)
		{
			View tab = (View) LayoutInflater.from(this).inflate(tabWidgetLayoutId, null);

			int j = -1;

			while (++j < tabWidgetIds.length)
			{
				View view = tab.findViewById(tabWidgetIds[j]);
				if (view instanceof TextView) ((TextView) view).setText(tabWidgetResIds[i][j]);
				if (view instanceof ImageView) ((ImageView) view).setImageResource(tabWidgetResIds[i][j]);
			}

			tabHost.addTab(tabHost.newTabSpec(lables[i]).setIndicator(tab).setContent(intents[i]));

		}

		init(tabHost);
	}

	private void init(TabHost tabHost)
	{
		tabHost.setOnTabChangedListener(this);
	}

	@Override
	public void onTabChanged(String tabId)
	{
		// TODO Auto-generated method stub
		
	}

	
}
