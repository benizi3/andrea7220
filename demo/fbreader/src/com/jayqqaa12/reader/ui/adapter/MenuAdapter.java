package com.jayqqaa12.reader.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayqqaa12.abase.util.ManageUtil;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.MenuItem;

public class MenuAdapter extends BaseAdapter
{
	List<MenuItem> data = new ArrayList<MenuItem>();

	public  MenuAdapter(){
		
		
		data.add(new MenuItem(R.drawable.icon_item_directory, "toc", MenuItem.ATION_TOC));
		data.add(new MenuItem(R.drawable.icon_item_bright, "night", MenuItem.ATION_NIGHT));
		data.add(new MenuItem(R.drawable.icon_bookshelf_set_up, "setting", MenuItem.ATION_SETTING));
		data.add(new MenuItem(R.drawable.icon_btn_font_big, "font_add", MenuItem.ATION_FONT_ADD));
		data.add(new MenuItem(R.drawable.icon_btn_font_small, "font_dim", MenuItem.ATION_FONT_DIM));
		data.add(new MenuItem(R.drawable.icon_item_progress, "progress", MenuItem.ATION_PROGRESS));
		
	}
	

	@Override
	public int getCount()
	{
		return data.size();
	}

	@Override
	public MenuItem getItem(int position)
	{
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = ManageUtil.getInflater().inflate(R.layout.test_menu_item, null);
		TextView tv = (TextView) view.findViewById(R.id.m_tv);
		ImageView iv = (ImageView) view.findViewById(R.id.m_iv);
		tv.setText(getItem(position).text);
		iv.setImageResource(getItem(position).icon);
		

		return view;
	}
	
	

}
