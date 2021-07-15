package com.jayqqaa12.abase.core.listener;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView.OnChildClickListener;

public interface Listener extends OnClickListener, OnLongClickListener, OnItemLongClickListener, OnItemSelectedListener, OnItemClickListener,
		OnCheckedChangeListener,OnScrollListener,  OnPageChangeListener, OnChildClickListener, OnTouchListener, OnFocusChangeListener,
		TextWatcher
{

}
