package com.jayqqaa12.abase.core.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jayqqaa12.abase.core.Abus;
import com.jayqqaa12.abase.core.ItemView;
import com.jayqqaa12.abase.kit.common.L;
import com.jayqqaa12.abase.kit.common.ReflectKit;

/**
 * 配合 android annotations使用 配合 itemView 使用
 * 
 * 可在 @afterInject 注入之后 设置 setItemView 传入 itemview 实例化类
 * 
 * 也可直接 new AbaseBaseAdapter<T>( itemview.class,context)
 * 
 * @author 12
 * 
 * @param <T>
 */
public class AAdapter<T> extends BaseAdapter
{
	
	public List<T> data = new ArrayList<T>();
	public Class clazz;
	public Context context;

	public AAdapter()
	{}

	public AAdapter(Class<? extends ItemView<T>> clazz, Context context)
	{
		setItemView(clazz, context);
	}

	public AAdapter(Class<? extends ItemView<T>> clazz, Context context, List<T> data)
	{
		setItemView(clazz, context);
		this.data = data;
	}

	public void setItemView(Class clazz, Context context)
	{
		this.clazz = ReflectKit.getSubClass(clazz);
		this.context = context;
	}

	public void setData(List<T> data)
	{
		this.data = data;
		this.notifyDataSetChanged();
	}
	
	
	public void addData(List<T> data)
	{
		this.data.addAll(data);
		
		this.notifyDataSetChanged();
	}

	@Override
	public T getItem(int position)
	{
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public int getCount()
	{
		return data.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return ItemView.bindView(context, clazz, convertView, getItem(position), this);
	}

}