package com.jayqqaa12.abase.core.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.jayqqaa12.abase.model.Group;
import com.jayqqaa12.abase.util.common.ReflectUtil;

public class AbaseExpandableListAdapter<T> extends BaseExpandableListAdapter
{

	private Class childClazz;
	private Class groupClazz;
	private Context context;

	private List<Group<T>> groups=new ArrayList<Group<T>>();
	
	
	
	public AbaseExpandableListAdapter(){
	}

	public AbaseExpandableListAdapter(Class groupClazz, Class childClazz,Context context){
		setItemView(groupClazz, childClazz, context);
	}
	public void setData(List<Group<T>> groups)
	{
		this.groups = groups;
		this.notifyDataSetChanged();
	}
 
	@Override
	public int getGroupCount()
	{
		return groups.size();
	}


	@Override
	public int getChildrenCount(int groupPosition)
	{
		return getGroup(groupPosition).chilren.size();
	}

	@Override
	public Group<T> getGroup(int groupPosition)
	{
		return groups.get(groupPosition);
	}

	@Override
	public T getChild(int groupPosition, int childPosition)
	{
		return getGroup(groupPosition).chilren.get(childPosition);
	}



	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	public void setItemView(Class groupClazz, Class childClazz,Context context)
	{

		this.groupClazz = ReflectUtil.getSubClass(groupClazz);
		this.childClazz = ReflectUtil.getSubClass(childClazz);
		this.context= context;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{

		return ItemView.bindView(context, groupClazz, convertView, getGroup(groupPosition));
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		return ItemView.bindView(context, childClazz, convertView, getChild (groupPosition,childPosition));
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}


}
