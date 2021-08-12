package com.ustore.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.android.service.R;
import com.jayqqaa12.abase.core.AbaseBitmap;
/**
 * 
 * @author way
 *详情全屏大图适配器
 */
public class FullImgPagerAdapter extends PagerAdapter {
	
	private LayoutInflater inflate;
	private String[] image;
	
	private AbaseBitmap bitmap  = AbaseBitmap.create();

	public FullImgPagerAdapter(Context context, String [] image) {
		
		this.image=image;
		inflate=LayoutInflater.from(context);
	}


	@Override
	public int getCount() {
		return image.length;
	}

	@Override
	public Object instantiateItem(View view, int pos) {
		
		ImageView img=(ImageView)inflate.inflate(R.layout.fullbimg1, null);
		
		bitmap.display(img, image[pos]);
		bitmap.configLoadfailImage(R.drawable.bg_bsimg);
		
		((ViewPager)view).addView(img, 0);
		return img;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
			
//			((ViewPager) container).removeViewAt(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
			return arg0 == (arg1);
	}



}
