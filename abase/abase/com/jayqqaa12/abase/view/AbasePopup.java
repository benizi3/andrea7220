package com.jayqqaa12.abase.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class AbasePopup extends PopupWindow
{
	private boolean isMenu=true;

	/***
	 * 必需 重写 initView
	 */
	public AbasePopup()
	{
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);

		View view = initView();

		this.setContentView(view);
		
		if(isMenu) {
			
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
	

	public AbasePopup(View view)
	{
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setOutsideTouchable(true);

		this.setContentView(view);
		if(isMenu) {
			
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

	protected View initView()
	{

		return null;
	};

	
	public void setIsMenu(boolean isMenu){
		
		this.isMenu=isMenu;
	}
}
