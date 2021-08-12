package com.ustore.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayqqaa12.abase.util.sys.SysIntentUtil;
import com.ustore.activity.Admin;
import com.ustore.activity.Home;
import com.ustore.activity.R;
import com.ustore.http.Website;

public class InstalledAdapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private ArrayList<Map<String, Object>> tmlist = null;
	private int layoutID;
	private String flag[] = null;
	private int ItemIDs[];
	private Activity activityname = null;

	public InstalledAdapter(Context context,
			ArrayList<Map<String, Object>> tmlist, int layoutID,
			String flag[], int ItemIDs[]) {
		this.mInflater = LayoutInflater.from(context);
		this.tmlist = tmlist;
		this.layoutID = layoutID;
		this.flag = flag;
		this.ItemIDs = ItemIDs;
	}

	public int getCount() {

		return tmlist.size();
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {

		return position;
	}

	public void setActivity(Activity name) {
		this.activityname = name;
		name = null;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(layoutID, null);
		}
		ImageView iv = null;
		TextView tv = null;
		
		TextView tv2=(TextView)convertView.findViewById(R.id.tv_bt_text);
		
		for (int i = 0; i < flag.length; i++) {

			if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {
				iv = (ImageView) convertView.findViewById(ItemIDs[i]);

				if (i == 2) {
					iv.setImageDrawable((Drawable) tmlist.get(position).get(
							flag[i]));
				} else if (i == 3) {
					iv.setBackgroundResource((Integer) tmlist.get(position)
							.get(flag[i]));
				}
				iv.setVisibility(View.VISIBLE);
			} else if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {
				tv = (TextView) convertView.findViewById(ItemIDs[i]);
				tv.setText((String) tmlist.get(position).get(flag[i]));
			}

		}
		iv = null;
		tv = null;
		/** type等于0表示未安装可以安装，等于1表示已安装可以卸载 **/
		int type = Integer.valueOf((String) tmlist.get(position).get(flag[5]));
		
		
	    int tag = Integer.valueOf((String) tmlist.get(position).get(flag[7]));
		
		switch (type) {
		case 0:
			tv2.setText(R.string.uninstall);
			break;
			
		case 1:
			tv2.setText(R.string.install);
			break;
		case 2:
			tv2.setText(R.string.update);
			break;
		}
		addListener(convertView, position, type, tag);
		return convertView;
	}

	/**
	 * 对相应的item设置监听，根据每个item实现跳转
	 * 
	 */
	public void addListener(final View convertView, final int position, final int type, final int tag) {
		ImageView button = null;
	
		//apk未安装，长按可删除
		if(type==1) {
			
			/*
			 * 删除apk
			 */
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println("long click");
					
					Dialog dialog = new AlertDialog.Builder(activityname).setTitle("")
							.setMessage("是否删除安装包")// 设置内容
							.setPositiveButton("确定",// 确定按钮
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											
											try {
												StringBuffer mSavePath1=null;
												if(tag==1) {
													mSavePath1 = new StringBuffer(Website.sd_path);
													mSavePath1.append("download");
												} else {
													 mSavePath1 = new StringBuffer(Website.fav_apk_path);
												}
												
												String apkname1 = (String) tmlist.get(position).get(flag[4]); // 得到app的名称
												File apkfile1 = new File(mSavePath1.toString(), apkname1);
												
												if (apkfile1.exists()) {
													apkfile1.delete();
												}
												tmlist.remove(position);
												notifyDataSetChanged();
												
											} catch (Exception e) {
												e.printStackTrace();
											}
											
										}
									}).setNegativeButton("取消", // 取消按钮
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int whichButton) {
											dialog.dismiss();
										}
									}).create();// 创建
					dialog.show();// 显示对话框
					
					dialog=null;
				}
			});	
		}
		
		button = (ImageView) convertView.findViewById(R.id.appbutton);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				switch (type) {
				
				// 卸载软件
				case 0:
					
					StringBuffer mSavePath = new StringBuffer(
							Website.sd_path);
					mSavePath.append("download");// sd卡下载文件的路径
					String apkname = (String) tmlist.get(position).get(flag[4]); // 得到app的名称
					File apkfile = new File(mSavePath.toString(), apkname);

					String packageName = (String) tmlist.get(position).get(
							flag[6]); // 得到软件的包名
					
					SysIntentUtil.uninstall(activityname, packageName);
					
					Intent intent = new Intent();// 刷新
					intent.setClass(activityname, Admin.class);
					intent.putExtra("page", 1);
					activityname.startActivity(intent);
					activityname.finish();
					activityname.overridePendingTransition(0, 0);// 禁用跳转动画效果
				
					break;
					
					// 安装软件
				case 1:
					
					try {
						StringBuffer mSavePath1=null;
						if(tag==1) {
							mSavePath1 = new StringBuffer(Website.sd_path);
							mSavePath1.append("download");
						} else {
							mSavePath1 = new StringBuffer(Website.fav_apk_path);
						}
						String apkname1 = (String) tmlist.get(position).get(flag[4]); // 得到app的名称
						File apkfile1 = new File(mSavePath1.toString(), apkname1);

						if (!apkfile1.exists()) {
							return;
						}
						// 通过Intent安装APK文件
						
						SysIntentUtil.install(activityname, apkfile1);
						
						
						Intent intent2 = new Intent();// 刷新
						intent2.putExtra("page", 1);
						intent2.setClass(activityname, Admin.class);
						activityname.startActivity(intent2);
						activityname.finish();
						activityname.overridePendingTransition(0, 0);// 禁用跳转动画效果
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
					
					// 更新软件
				case 2:
					
					break;

				}
				
				
			
			}
		});

	}
}