package com.jayqqaa12.abase.core.listener;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class AListViewListener implements OnScrollListener, OnItemClickListener, OnLoadStatus
{
	/***
	 * 当前的 最后一个 列表项的位置
	 */
	protected int endPostion;
	/***
	 * 刷新后 记得最前的最后一个
	 */
	protected int prepostion;
	/***
	 * 加载状态
	 */
	protected int status;

	
	@Override
	public void onLoadStatus(int what, Object obj)
	{
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		
	}

}
