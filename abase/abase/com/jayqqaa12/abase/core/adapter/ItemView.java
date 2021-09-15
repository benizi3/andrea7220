package com.jayqqaa12.abase.core.adapter;

import java.lang.reflect.Method;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

@EViewGroup
public abstract class ItemView<T> extends LinearLayout
{
	public abstract void bind(T item);

	public ItemView(Context context)
	{
		super(context);
	}

	/***
	 * 此方法可bindview
	 * 
	 * @return
	 */
	public static ItemView  bindView(Context context, Class clazz, View convertView, Object bindObj)
	{

		ItemView view = null;
		try
		{
			if (clazz == null || context == null) throw new NullPointerException(" must set item view class and context");

			if (convertView == null)
			{
				Method m = clazz.getMethod("build", new Class[] { Context.class });
				view = (ItemView) m.invoke(clazz, new Object[] { context });
			}
			else view = (ItemView) convertView;
			view.bind(bindObj);

		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)

		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return view;

	}

}
