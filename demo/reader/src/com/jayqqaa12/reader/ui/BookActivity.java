package com.jayqqaa12.reader.ui;

import java.io.IOException;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.sharedpreferences.Pref;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.reader.Pref_;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.Book;
import com.jayqqaa12.reader.ui.view.PageFactory;
import com.jayqqaa12.reader.ui.view.PageView;

@SuppressLint("WrongCall")
@Fullscreen
@NoTitle
@EActivity
public class BookActivity extends Activity implements OnTouchListener
{

	private PageView pageView;
	private Bitmap curPageBitmap, nextPageBitmap;
	private Canvas curPageCanvas, nextPageCanvas;
	private PageFactory pagefactory;

	@Pref
	Pref_ pref;
	
	@Extra
	Book book;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Display display = getWindowManager().getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();
		curPageBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		nextPageBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

		curPageCanvas = new Canvas(curPageBitmap);
		nextPageCanvas = new Canvas(nextPageBitmap);
		
		pagefactory = new PageFactory(w, h);
		pagefactory.setBgBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg));

		if (book != null)
		{
			pagefactory.setFileName(book.name);
			pageView = new PageView(this, w, h);
			setContentView(pageView);
			pagefactory.openbook(book.path);
			int m_mbBufLen = pagefactory.getBufLen();

			if (book.progress > 0)
			{
				 pagefactory.setFontSize(pref.font().get());
				 
				 pagefactory.setBeginPos(pref.progress().get());
				 
				try
				{
					pagefactory.prePage();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				pagefactory.onDraw(nextPageCanvas);
				pageView.setBitmaps(nextPageBitmap, nextPageBitmap);
				pageView.postInvalidate();
			}
			else
			{
				pagefactory.onDraw(curPageCanvas);
				pageView.setBitmaps(curPageBitmap, curPageBitmap);
			}

			pageView.setOnTouchListener(this);

		}
		else
		{
			Toast.makeText(this, "不存在", Toast.LENGTH_SHORT).show();
			BookActivity.this.finish();
		}

	}
	
	private static long startTime =System.currentTimeMillis();
	private static long endTime =System.currentTimeMillis();

	@Override
	public boolean onTouch(View v, MotionEvent e)
	{
		endTime =System.currentTimeMillis();
		
		
		if(endTime-startTime>0&& endTime-startTime<333&&e.getAction() == MotionEvent.ACTION_DOWN){
			
			return false;
		}
		
		startTime= System.currentTimeMillis();
		
		boolean ret = false;
		if (v == pageView)
		{
			if (e.getAction() == MotionEvent.ACTION_DOWN)
			{
				pageView.abortAnimation();
				pageView.calcCornerXY(e.getX(), e.getY());

				pagefactory.onDraw(curPageCanvas);
				if (pageView.DragToRight())
				{
					try
					{
						pagefactory.prePage();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
					if (pagefactory.isfirstPage())
					{
						Toast.makeText(this, "已经是第一页", Toast.LENGTH_SHORT).show();
						return false;
					}
					pagefactory.onDraw(nextPageCanvas);
				}
				else
				{
					try
					{
						pagefactory.nextPage();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
					if (pagefactory.islastPage())
					{
						Toast.makeText(this, "last", Toast.LENGTH_SHORT).show();
						return false;
					}
					pagefactory.onDraw(nextPageCanvas);
				}
				pageView.setBitmaps(curPageBitmap, nextPageBitmap);
			}
			ret = pageView.doTouchEvent(e);
			return ret;
		}
		return false;
	}

}
