package com.jayqqaa12.abase.core.adapter;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.widget.LinearLayout;

@EViewGroup
public  abstract class ItemView<T> extends LinearLayout
{
	public abstract void bind(T item);

	public ItemView(Context context)
	{
		super(context);
	}

	
	

}
