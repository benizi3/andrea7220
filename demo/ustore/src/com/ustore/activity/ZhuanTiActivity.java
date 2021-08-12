package com.ustore.activity;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jayqqaa12.abase.core.ACache;
import com.jayqqaa12.abase.core.AbaseBitmap;
import com.jayqqaa12.abase.core.AbaseHttp;
import com.ustore.http.Website;
import com.ustore.util.ValidateData;

/**
 * 专题
 */
public class ZhuanTiActivity extends Activity
{
	private static final String TAG_NAME = "专题";

	GridView gridView;

	private volatile ArrayList<String[]> IDlist = new ArrayList<String[]>();
	private List<String> imgurls = new ArrayList<String>();

	private int num;// 专题图片个数

	TextView title;
	ImageView top1;
	ProgressBar p1;
	GridAdapter gridAdapter;

	private ACache cache = ACache.create();

	private AbaseBitmap bitmap = AbaseBitmap.create();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhuanti);
		title = (TextView) findViewById(R.id.t1);
		title.setText(TAG_NAME);
		top1 = (ImageView) findViewById(R.id.top1);

		p1 = (ProgressBar) findViewById(R.id.p1);

		gridView = (GridView) findViewById(R.id.gridview);
		gridAdapter = new GridAdapter();

		concet();

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (Home.isDownLoading) top1.setVisibility(View.VISIBLE);
		else top1.setVisibility(View.GONE);
	}

	private void concet()
	{
		String c = null;
		c = cache.getAsString(Website.subject_image_id);
		if (c != null) setDate(c);
		else
		{
			new AbaseHttp().get(Website.subject_image_id, new AjaxCallBack<String>()
			{
				@Override
				public void onSuccess(String t)
				{
					if (ValidateData.validate(t))
					{
						setDate(t);
						ACache.create().put(Website.subject_image_id, t, ACache.TIME_DAY * 3);
					}
					else concet();
				}

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg)
				{

					Dialog dialog = new AlertDialog.Builder(ZhuanTiActivity.this).setMessage("网络连接失败 是否重试").setPositiveButton("重试",// 确定按钮
							new android.content.DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
									concet();
								}
							}).setNegativeButton("退出", new android.content.DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							ZhuanTiActivity.this.finish();
						}
					}).create();
					dialog.show();
				}
			});

		}

	}

	private void setDate(String c)
	{
		String[] c2 = c.split("\\}\\{");

		final String[] c3 = (c2[1].substring(0, c2[1].length() - 1)).split("\\|");

		try
		{

			for (int i = 0; i < c3.length; i++)
			{
				final String[] data = new String[3];
				data[0] = c3[i].split("\\^")[0];
				data[1] = c3[i].split("\\^")[1];
				data[2] = c3[i].split("\\^")[2];

				String url = Website.subject_image_url + data[0] + ".png";
				IDlist.add(data);// 把图片ID添加到list
				imgurls.add(url);
			}
		}
		catch (Exception e)
		{
			cache.remove(Website.subject_image_id);
			concet();
			
		}

		num = imgurls.size();
		gridView.setAdapter(gridAdapter);// 添加到适配
		setListener();

	}

	// for test
	private int[] aa = { R.drawable.z01, R.drawable.z02, R.drawable.z03, R.drawable.z04 };

	private class GridAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			if (IDlist != null) { return IDlist.size(); }

			return 0;

		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView;

			if (convertView == null)
			{
				convertView = new ImageView(ZhuanTiActivity.this);
			}

			imageView = (ImageView) convertView;
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);

			int h = ZhuanTiActivity.this.getWindowManager().getDefaultDisplay().getHeight();

			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, h / 4));

			bitmap.configLoadingImage(R.drawable.logo);
			bitmap.display(imageView, imgurls.get(position));

			return convertView;

		}

	}

	public void onClickBar(View v)
	{

		Intent intent = new Intent();

		switch (v.getId())
		{

		// 搜索
		case R.id.top2:
			intent.setClass(this, Search.class);
			break;

		// 管理
		case R.id.top3:
			intent.setClass(this, Admin.class);
			break;

		}
		startActivity(intent);

		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	/**
	 * 点击返回按钮退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{// 点击back键

			finish();// 退出程序
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

		}

		return super.onKeyDown(keyCode, event);
	}

	private void setListener()
	{

		gridView.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{

				if (num == 0)
				{// 如果联网失败无法获取图片个数，发出提示信息，并中断下面的操作
					Toast.makeText(ZhuanTiActivity.this, "网络错误,请连接网络!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (position >= num)
				{
					position = position % num;// 取余得到第几张图片位置
				}

				String subjectUrl = Website.subject_url + IDlist.get(position)[0];// 专题链接地址
				String name = "subject" + IDlist.get(position)[0];// 存储到内存卡的文件名
				Intent intent = new Intent();
				intent.setClass(ZhuanTiActivity.this, Subject.class);
				intent.putExtra("url", subjectUrl);// 将地址传递到专题类中
				intent.putExtra("name", name);// 将文件名传递到专题类中

				intent.putExtra("title", IDlist.get(position)[1]);
				intent.putExtra("description", IDlist.get(position)[2]);

				startActivity(intent);// 跳转到专题

				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});

		gridView.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				ImageView imageView = (ImageView) view;
				imageView.setAlpha(100);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		p1.setVisibility(View.GONE);
	}
}
