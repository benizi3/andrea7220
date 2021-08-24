package com.jayqqaa12.abase.view;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.jayqqaa12.abase.core.Abase;
import com.jayqqaa12.abase.util.IntentUtil;

public class AbasePopup extends PopupWindow
{
	private boolean isMenu = true;

	/***
	 * 必需 重写 initView
	 */
	public AbasePopup()
	{
		setView();
		View view = initView();
		this.setContentView(view);
		setBack(view);
	}


	public AbasePopup(Class<? extends BindView> clazz)
	{
		setView();
		View view = null;
		try
		{
			Method m = IntentUtil.getSubClass(clazz).getMethod("build", new Class[] { Context.class });
			view = (BindView) m.invoke(clazz, new Object[] { Abase.getContext() });

			((BindView) view).bind();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		this.setContentView(view);
		setBack(view);
	}

	public AbasePopup(View view)
	{
		setView();
		this.setContentView(view);
		setBack(view);
	}

	
	/**
	 * 使用 bindview 的时候使用
	 * @return
	 */
	public BindView getBindView(){
		return (BindView) getContentView();
	}

	
	protected View initView()
	{

		return null;
	};

	
	
	private void setBack(View view)
	{
		if (isMenu)
		{

			view.setFocusableInTouchMode(true);
			view.setOnKeyListener(new OnKeyListener()
			{
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if ((keyCode == KeyEvent.KEYCODE_MENU) && AbasePopup.this.isShowing())
					{
						AbasePopup.this.dismiss();
						return true;
					}
					return false;
				}
			});
		}
	}

	private void setView()
	{
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);
	}
	public void setIsMenu(boolean isMenu)
	{

		this.isMenu = isMenu;
	}
}
