package com.ustore.adapter;

import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.service.R;
import com.jayqqaa12.abase.util.network.DownLoadUtil;

public class DetailsAdapter extends BaseAdapter {
	
		private LayoutInflater mInflater = null;
		private HashMap<String, String> list = null;
		private int layoutID;
		private String flag[] = null;
		private int ItemIDs[];	
		public DetailsAdapter(Context context, HashMap<String, String> mlist,
				int layoutID, String flag[], int ItemIDs[]) {
			this.mInflater = LayoutInflater.from(context);
			this.list = mlist;
			this.layoutID = layoutID;
			this.flag = flag;
			this.ItemIDs = ItemIDs;
		}
		public int getCount() {
			return 1;
		}

		public Object getItem(int arg0) {
			return 0;
		}

		public long getItemId(int arg0) {			
			return 0;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView ==null) {
				convertView = mInflater.inflate(layoutID, null);
			}
			ImageView iv=null;
			TextView tv=null;
			for (int i = 0; i < flag.length; i++) {	
			
			    if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {
			    	
			    	Drawable drawable =	DownLoadUtil.getDrawable(list.get(flag[i]));
			    	
			    	if(i == 17 ){//详情图标实例化
						iv = (ImageView) convertView.findViewById(ItemIDs[i]);
						try {							
							iv.setImageDrawable(drawable);						
						} catch (Exception e) {							
							iv.setImageResource(R.drawable.default_icon);
						}
						
					}
			    	else if(i==18){//详情浏览大图实例化
			    		iv = (ImageView) convertView.findViewById(ItemIDs[i]);
						try {
							iv.setImageDrawable(drawable);
						} catch (Exception e) {							
							iv.setImageResource(R.drawable.bg_bsimg);
						}						
					}
			    	try
					{
						drawable.setCallback(null);			
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
			    	
				} else if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {
					tv = (TextView) convertView.findViewById(ItemIDs[i]);
					tv.setText(list.get(flag[i]));
					
				}
				
			}
			
	
			iv=null;
			tv=null;
			return convertView;
		}
}
