package com.ustore.adapter;

import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jayqqaa12.abase.core.AbaseBitmap;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.T;
import com.ustore.activity.R;
import com.ustore.download.Dao;
import com.ustore.service.DownloadService;

public class DownloadAdapter extends BaseAdapter
{
	private LayoutInflater mInflater = null;
	private Map<String, String[]> tmlist = null;
	private int layoutID;
	private String flag[] = null;
	private int ItemIDs[];
	private Context mContext;
	private AbaseBitmap bitmap =  AbaseBitmap.create();

	public DownloadAdapter(Context context, Map<String, String[]> tmlist, int layoutID, String flag[], int ItemIDs[])
	{

		this.mInflater = LayoutInflater.from(context);
		this.tmlist = tmlist;
		this.layoutID = layoutID;
		this.flag = flag;
		this.ItemIDs = ItemIDs;
		this.mContext = context;

	}

	public void setData(Map<String, String[]> data)
	{

		this.tmlist = data;
		notifyDataSetChanged();
	}

	public int getCount()
	{

		return tmlist.size();
	}

	public Object getItem(int position)
	{

		return position;
	}

	public long getItemId(int position)
	{

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{

		if (convertView == null)
		{
			convertView = mInflater.inflate(layoutID, null);
		}

		/**
		 * state=0暂停, state=1下载
		 */
		int state = -1;

		String name = tmlist.get(String.valueOf(position))[0];
		String id = tmlist.get(String.valueOf(position))[3];
		String imgurl = tmlist.get(String.valueOf(position))[4]; // add

		ImageView iv1 = (ImageView) convertView.findViewById(R.id.iv_bt);
		TextView tv1 = (TextView) convertView.findViewById(R.id.tv_bt_text);

		switch (Integer.valueOf(tmlist.get(String.valueOf(position))[5]))
		{
		/**
		 * 下载中,点击暂停
		 */
		case 0:
			state = 0;
			iv1.setBackgroundResource(R.drawable.pause);
			tv1.setText(R.string.pause);
			break;

		/**
		 * 暂停，点击下载
		 */
		case 1:
			state = 1;
			iv1.setBackgroundResource(R.drawable.down);
			tv1.setText(R.string.download);
			break;

		/**
		 * 等待
		 */
		case 2:
			tv1.setText(R.string.wait);
			break;

		default:
			break;
		}

		/**
		 * 下载中,点击暂停
		 */

		ImageView iv = null;
		TextView tv = null;
		ProgressBar pBar = null;
		for (int i = 0; i < flag.length; i++)
		{

			if (convertView.findViewById(ItemIDs[i]) instanceof ImageView)
			{
				iv = (ImageView) convertView.findViewById(ItemIDs[i]);

				bitmap.configLoadingImage(R.drawable.default_icon);
				bitmap.configLoadfailImage(R.drawable.default_icon);
				bitmap.display(iv, imgurl);

			}

			else if (convertView.findViewById(ItemIDs[i]) instanceof ProgressBar)
			{

				pBar = (ProgressBar) convertView.findViewById(ItemIDs[i]);

				pBar.setIndeterminate(false);
				pBar.setIndeterminateDrawable(mContext.getResources().getDrawable(R.anim.loading));

				pBar.setProgress(Integer.valueOf(tmlist.get(String.valueOf(position))[i]));

			}
			else if (convertView.findViewById(ItemIDs[i]) instanceof TextView)
			{
				tv = (TextView) convertView.findViewById(ItemIDs[i]);
				tv.setText(tmlist.get(String.valueOf(position))[i]);

			}
		}

		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < tmlist.size(); i++)
		{
			strBuffer.append("[0]").append(tmlist.get(String.valueOf(i))[0]).append("\n").append("[1]")
					.append(tmlist.get(String.valueOf(i))[1]).append("\n").append("[2]").append(tmlist.get(String.valueOf(i))[2])
					.append("\n").append("[3]").append(tmlist.get(String.valueOf(i))[3]).append("\n\n");
		}

		addListener(convertView, position, state, id, name, imgurl);
		return convertView;
	}

	/**
	 * 对相应的item设置监听，根据每个item实现跳转
	 * 
	 */
	public void addListener(View convertView, final int position, final int state, final String id, final String name, final String icon)
	{

		convertView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				if (!DownloadService.canotPauseSet.contains(id))
				{
					Dialog dialog = new AlertDialog.Builder(mContext).setTitle("").setMessage("是否删除下载")// 设置内容
							.setPositiveButton("确定",// 确定按钮
									new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog, int which)
										{

											DownloadService.removeTaskAndDeleteFile(id);

											notifyDataSetChanged();

										}
									}).setNegativeButton("返回", // 取消按钮
									new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog, int whichButton)
										{
											dialog.dismiss();
										}
									}).create();// 创建
					dialog.show();// 显示对话框
				}
			}
		});

		((ImageView) convertView.findViewById(R.id.iv_bt)).setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{

				switch (state)
				{
				// 暂停
				case 0:

					if (!DownloadService.canotPauseSet.contains(id)) DownloadService.stopTaskNotStopNotifiction(id);
					else T.ShortToast("当前下载不可暂停");

					break;

				// 下载
				case 1:

					if (DownloadService.downloadSet.contains(id))
					{
						L.i("已经在下载队列 ");
						return;
					}

					if (Dao.getInstance().getInfo(id) != null)
					{
						String url = Dao.getInstance().getInfo(id).getUrl();
						mContext.startService(DownloadService.getSendIntent(mContext, id, name, icon, url, false));
					}

					break;

				}

			}
		});
	}

}
