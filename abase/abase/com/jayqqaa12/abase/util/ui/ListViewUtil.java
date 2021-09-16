package com.jayqqaa12.abase.util.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/***
 * listview 
 * @author 12
 *
 */
public class ListViewUtil
{
	
	/***
	 * 
	 * 请在 setAdapter 之前 添加
	 * 
	 *  下拉 刷新 显示的页脚
	 * @param lv
	 * @param context
	 * @param animRes
	 * @return
	 */
	public static LinearLayout addLoadingFooter(ListView lv,Context context,int animRes)
	{
		LinearLayout searchLayout = new LinearLayout(context);
		// 水平方向的线性布局
		searchLayout.setOrientation(LinearLayout.HORIZONTAL);
		// 添加进展条
		ProgressBar progressBar = new ProgressBar(context);

		progressBar.setIndeterminate(false);
		progressBar.setIndeterminateDrawable(context.getResources().getDrawable(animRes));
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

		LinearLayout loadingLayout = new LinearLayout(context);
		loadingLayout.addView(searchLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		loadingLayout.setGravity(Gravity.CENTER);

		lv.addFooterView(loadingLayout);

		return loadingLayout;
	}
}
