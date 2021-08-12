package com.ustore.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.jayqqaa12.abase.core.AbaseBitmap;
import com.ustore.activity.R;
import com.ustore.bean.HomeGuideBean;

/** 专题图片适配器 **/
public class SubjectImageAdapter extends BaseAdapter
{

	private final Context mContext;
	private List<HomeGuideBean> mylist;

	private int[] aa = { R.drawable.z2, R.drawable.z3, R.drawable.z4, R.drawable.z5, R.drawable.z6 };

	private AbaseBitmap bitmap = AbaseBitmap.create();

	public SubjectImageAdapter(Context c, List<HomeGuideBean> list)
	{
		mContext = c;
		mylist = list;

	}

	public int getCount()
	{

		return Integer.MAX_VALUE;
	}

	public Object getItem(int position)
	{
		// modify
		return position % mylist.size();

	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;

		if (convertView == null)
		{
			convertView = new ImageView(mContext);
			imageView = (ImageView) convertView;// 得到View
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setBackgroundResource(R.drawable.logo);
		}
		else
		{
			imageView = (ImageView) convertView;
		}

		if (mylist != null&&mylist.size() > 0)
		{
				position = position % mylist.size();// 通过取余得到当前位置
				
				bitmap.configLoadingImage(R.drawable.logo);
				bitmap.configLoadfailImage(R.drawable.logo);
				bitmap.display(imageView, mylist.get(position).imgurl);
		}
		else imageView.setImageResource(R.drawable.baidu);// 显示专题图片

		return convertView;
	}
}
