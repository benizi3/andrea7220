package com.ustore.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jayqqaa12.abase.core.AbaseBitmap;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.T;
import com.ustore.activity.DownDetails;
import com.ustore.activity.Home;
import com.ustore.activity.R;
import com.ustore.bean.DownloadListInfo;
import com.ustore.data.CatchData;
import com.ustore.data.Flag;
import com.ustore.download.Dao;
import com.ustore.download.Database;
import com.ustore.http.Website;
import com.ustore.service.DownloadService;

/** 各页面ListView数据适配器 **/
public class MySimpleAdapter extends BaseAdapter
{
	private Flag Flag = new Flag();
	private LayoutInflater mInflater;
	private Vector<CatchData> mModels = null;
	private ListView mListView;
	/** Activity名字 **/
	private Activity context;
	/** listView的最后一天数据 **/
	private int lastItem = 0;
	/** 翻页时,listView底部进度条的布局 **/
	private static LinearLayout loadingLayout;
	/** 每页加载的条数 **/
	private int size = 10;
	private int add = 8;
	/** 记录ListView显示的位置 **/
	public int location = 0;
	/** 保存Activity要显示的ListView的位置 **/
	public static HashMap<String, Flag> flag = new HashMap<String, Flag>();
	private String mflag = null;
	private PackageManager packageManager;

	private AbaseBitmap bitmap =  AbaseBitmap.create();
	
	
	

	/** 构造方法 **/
	public MySimpleAdapter(Activity context, ListView listView, Vector<CatchData> model, String mfla)
	{
		mInflater = LayoutInflater.from(context);
		mListView = listView;
		this.context = context;
		mListView.setOnScrollListener(onScrollListener);
		clean();
		mModels = model;
		nextlist();
		mflag = mfla;

		if (mflag != "search" || !mflag.equals("search"))
		{
			if (flag.containsKey(mflag))
			{
				size = flag.get(mflag).size;
//				location = flag.get(mflag).location;
			}
		}

		packageManager = context.getPackageManager();
		
		context.registerReceiver(new UpdateReceiver(), new IntentFilter("com.ustore.download.update"));

	}

	private class UpdateReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent != null)
			{

				int status = intent.getIntExtra("status", -1);
				String id = intent.getStringExtra("id");
				L.i("catch  receiver status = " + status + " id =" + id);

				if (id != null)
				{
					for (CatchData cd : mModels)
					{
						if (cd.id.equals(id))
						{
							L.i("status change");
							cd.state = status;
							notifyDataSetChanged();
						}
					}
				}
			}
		}
	}

	/** 清除ListView底部动态加载进度条 ***/
	public void clean()
	{
		if (loadingLayout != null)
		{
			mListView.removeFooterView(loadingLayout);
		}

	}

	/** 显示的Item条数 **/
	public int getCount()
	{
		int i = mModels.size() / size;
		int j = 0;
		if (j < i)
		{
			return size;
		}
		else
		{
			return mModels.size();
		}
	}

	public Object getItem(int position)
	{
		if (position >= getCount()) { return null; }
		return mModels.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	/** 适配到数据和图片 **/
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.listview, null);
		}

		CatchData model = mModels.get(position);
		convertView.setTag(position);
		ImageView iv = (ImageView) convertView.findViewById(R.id.icon);
		ImageView btn = (ImageView) convertView.findViewById(R.id.btn);
		TextView btnText = (TextView) convertView.findViewById(R.id.tv_bt_text);

		switch (model.state)
		{

		// 下载
		case 0:
			btn.setBackgroundResource(R.drawable.down);
			btnText.setText(R.string.download);
			break;
		// 启动
		case 1:
			btn.setBackgroundResource(R.drawable.init);
			btnText.setText(R.string.boot);
			break;

		// 更新
		case 2:
			btn.setBackgroundResource(R.drawable.update);
			btnText.setText(R.string.update);
			break;

		// 暂停
		case 3:

			btn.setBackgroundResource(R.drawable.pause);
			btnText.setText(R.string.pause);
			break;

		default:
			break;
		}

		TextView sItemTitle = (TextView) convertView.findViewById(R.id.TextView01);
		TextView content = (TextView) convertView.findViewById(R.id.TextView02);

		StringBuffer str = new StringBuffer(model.downloadTimes);

		try
		{
			str.append("次下载, ").append(new DecimalFormat("##0.00").format((Float.valueOf(model.size) / (1024 * 1024))) + "MB");

		}
		catch (Exception e)
		{
			if (model.size.contains("KB")) str.append(model.size);

		}
		content.setText(str.toString());

		ImageView star = (ImageView) convertView.findViewById(R.id.star);
		sItemTitle.setText(model.name);// 显示每个软件的名字
		star.setBackgroundResource(R.drawable.star_5);// 显示5颗星

		bitmap.configLoadingImage(R.drawable.default_icon);
		bitmap.configLoadfailImage(R.drawable.default_icon);
		
		bitmap.display(iv, model.icon);

		addListener(convertView, position);// 监听
		return convertView;
	}

	/** 滚动监听 **/
	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener()
	{

		public void onScrollStateChanged(AbsListView view, int scrollState)
		{

			switch (scrollState)
			{
			case OnScrollListener.SCROLL_STATE_IDLE:// 滚动停止时状态

				location = mListView.getFirstVisiblePosition(); // ListPos记录当前可见的List顶端的一行的位置
				if (mflag != "search" || !mflag.equals("search"))
				{// 记录每个页面滚动的位置和显示的数量
					try
					{
						Flag.size = size;
						Flag.location = location;

					}
					catch (Exception e)
					{

					}

					flag.put(mflag, Flag);// 将位置和数量保存到flag
				}

				System.out.println("lastItem:" + lastItem + ", getCount:" + getCount() + ", headerView:" + mListView.getHeaderViewsCount());

				if (lastItem == getCount())
				{// 滚动时符合本条件时加载下一页

					int totle = mModels.size();
					size += add; // 添加下页数据
					if (totle - size > 0)
					{
						notifyDataSetChanged();// 刷新listView菜单
					}
					else
					{
						size = totle;
						mListView.removeFooterView(loadingLayout);
						notifyDataSetChanged();// 刷新listView菜单

					}

				}

				break;
			}

		}

		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{// 滚动状态

			lastItem = firstVisibleItem + visibleItemCount - 1 - mListView.getHeaderViewsCount();
		}
	};

	/** ListView添加下一页的加载进度布局实例化 **/
	@SuppressWarnings("deprecation")
	private void nextlist()
	{

		if (mModels.size() > add)
		{
			LinearLayout searchLayout = new LinearLayout(context);
			// 水平方向的线性布局
			searchLayout.setOrientation(LinearLayout.HORIZONTAL);
			// 添加进展条
			ProgressBar progressBar = new ProgressBar(context);

			progressBar.setIndeterminate(false);
			progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.loading));
			progressBar.setPadding(0, 0, 11, 0);
			searchLayout.addView(progressBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));

			// 添加文字，设置文字垂直居中
			TextView textView = new TextView(context);
			textView.setText("加载中...");
			textView.setGravity(Gravity.CENTER_VERTICAL);
			searchLayout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));

			// 同时将进展条和加载文字显示在中间
			searchLayout.setGravity(Gravity.CENTER);

			if (loadingLayout == null)
			{

				loadingLayout = new LinearLayout(context);
				loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				loadingLayout.setGravity(Gravity.CENTER);

			}

			mListView.addFooterView(loadingLayout);

		}
	}

	/**
	 * 对相应的item设置监听，根据每个item实现跳转
	 * 
	 */
	public void addListener(final View convertView, final int position)
	{

		ImageView btn = (ImageView) convertView.findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CatchData model = mModels.get(position);
				switch (model.state)
				{
				// download
				case 0:
					Animation a = AnimationUtils.loadAnimation(context, R.anim.shake);
					convertView.startAnimation(a);

					a.setAnimationListener(new AnimationListener()
					{

						@Override
						public void onAnimationStart(Animation arg0)
						{
						}

						@Override
						public void onAnimationRepeat(Animation arg0)
						{
						}

						@Override
						public void onAnimationEnd(Animation arg0)
						{
							mModels.get(position).state = 3;
							notifyDataSetChanged();

							beginDownload(position);
						}
					});


					context.sendBroadcast(new Intent("com.ustore.downloadreceiver"));

					break;

				// update
				case 2:

					String apkid = mModels.get(position).id;
					StringBuffer mSavePath = new StringBuffer(Website.sd_path);
					mSavePath.append("download");
					StringBuffer apkname = new StringBuffer(apkid);
					apkname.append(".apk");// 软件安装包的名称
					File apkFile = new File(mSavePath.toString(), apkname.toString());

					Dao d = Dao.getInstance(context);

					// 旧版本未下载完成，删除旧的下载信息
					if (!d.notHasInfors(apkid))
					{
						d.delete(apkid);
						// 有更新，删除之前未下载完成的apk
						if (apkFile.exists())
						{
							apkFile.delete();

						}
					}

					// 更新数据库中对应的apk版本号
					Database db = null;
					try
					{
						db = new Database(context);
						db.updateApkVersion(model.id, model.version);

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						if (db != null)
						{
							db.close();
						}
					}

					beginDownload(position);
					Home.isDownLoading = true;
					mModels.get(position).state = 3;
					notifyDataSetChanged();

					context.sendBroadcast(new Intent("com.ustore.downloadreceiver"));

					break;

				// pause 暂停
				case 3:

					if (!DownloadService.canotPauseSet.contains(mModels.get(position).id))
					{
						DownloadService.stopTaskNotStopNotifiction(mModels.get(position).id);
						mModels.get(position).state = 0;
						notifyDataSetChanged();
					}
					else
					{
						T.ShortToast("当前下载 不可暂停");
					}

					break;

				// start 启动
				case 1:

					try
					{

						File f = new File(Website.sd_path + "download", model.id + ".apk");
						PackageInfo info = null;
						try
						{
							info = packageManager.getPackageArchiveInfo(f.toString(), PackageManager.GET_ACTIVITIES);
						}
						catch (Exception e)
						{}

						if (info != null)
						{

							ApplicationInfo appInfo = info.applicationInfo;
							String packageName = appInfo.packageName;
							Intent intent = packageManager.getLaunchIntentForPackage(packageName);

							// 已安装此apk
							if (intent != null)
							{

								context.startActivity(intent);

								// 未安装
							}
							else
							{
								Toast.makeText(context, "请先安装!", Toast.LENGTH_SHORT).show();
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setDataAndType(Uri.parse("file://" + f.toString()), "application/vnd.android.package-archive");
								context.startActivity(i);
							}

							// 安装包解析失败
						}
						else
						{
							Toast.makeText(context, "安装包错误,请重新下载", Toast.LENGTH_SHORT).show();
							if (f.exists())
							{
								f.delete();
							}

							model.state = 0;
							notifyDataSetChanged();

						}

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					break;
				}

			}
		});

		convertView.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				Intent intent = new Intent();
				CatchData model = mModels.get(position);
				int i = Integer.valueOf(model.id);
				if (0 == i)
				{
					Toast.makeText(context, "网络数据丢失!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					intent.setClass(context, DownDetails.class);
					// 将id传递到DownDetails中
					intent.putExtra("id", model.id);
					intent.putExtra("pos", position);
					intent.putExtra("url", model.url);
				}

				context.startActivity(intent);
				context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

			}
		});

	}

	/**
	 * 启动下载的线程,给出下载提示
	 */
	public void beginDownload(int position)
	{
		try
		{
			final String myid = mModels.get(position).id;// 软件ID
			final String mynameString = mModels.get(position).name;// 软件名称
			String icon = mModels.get(position).icon; // 软件小图标
			String url = mModels.get(position).url;

			DownloadListInfo downInfo = new DownloadListInfo(myid, mynameString, icon, "0");
			Dao.getInstance().saveDownloadListInfo(downInfo);

			context.startService(DownloadService.getSendIntent(context, myid, mynameString, icon, url,false));
			Home.isDownLoading = true;

		}
		catch (Exception e)
		{

			e.printStackTrace();
		}

	}


}
