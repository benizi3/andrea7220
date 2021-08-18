package com.jayqqaa12.filemanage;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayqqaa12.reader.R;

class FileAdapter extends BaseAdapter
{
	private Bitmap mBackUp;
	private Bitmap mFolder;
	private Bitmap mOthers;
	private Bitmap mTxt;
	private Context mContext;
	private List<String> mFileNameList;
	private List<String> mFilePathList;

	public FileAdapter(Context context, List<String> fileName, List<String> filePath)
	{
		mContext = context;
		mFileNameList = fileName;
		mFilePathList = filePath;
		mBackUp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.back_to_up);
		
		mOthers = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.others);
		mFolder = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.folder);
		mTxt = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.txt);
		
	}

	public int getCount()
	{
		return mFilePathList.size();
	}

	public Object getItem(int position)
	{
		return mFileNameList.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	// 获得视图
	public View getView(int position, View convertView, ViewGroup viewgroup)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// 初始化列表元素界面
			convertView = mLI.inflate(R.layout.lv_file, null);
			// 获取列表布局界面元素
			viewHolder.mIV = (ImageView) convertView.findViewById(R.id.lv_iv);
			viewHolder.mTV = (TextView) convertView.findViewById(R.id.lv_tv);
			// 将每一行的元素集合设置成标签
			convertView.setTag(viewHolder);
		}
		else viewHolder = (ViewHolder) convertView.getTag();

		File mFile = new File(mFilePathList.get(position).toString());

		if (mFileNameList.get(position).toString().equals("BacktoUp"))
		{
			// 添加返回上一级菜单的按钮
			viewHolder.mIV.setImageBitmap(mBackUp);
			viewHolder.mTV.setText("返回上一级");
		}
		else
		{
			String fileName = mFile.getName();
			viewHolder.mTV.setText(fileName);
			if (mFile.isDirectory())
			{
				viewHolder.mIV.setImageBitmap(mFolder);
			}
			else
			{
				String fileEnds = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();// 取出文件后缀名并转成小写
				if (fileEnds.equals("txt")) viewHolder.mIV.setImageBitmap(mTxt);
				else viewHolder.mIV.setImageBitmap(mOthers);
			}
		}
		return convertView;
	}

	class ViewHolder
	{
		ImageView mIV;
		TextView mTV;
	}
}
