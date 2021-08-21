package com.jayqqaa12.reader.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jayqqaa12.abase.core.AbaseDao;
import com.jayqqaa12.abase.util.io.FileUtil;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.Book;
import com.jayqqaa12.reader.model.BookFile;

@EActivity(R.layout.activity_dialog_delete_book)
public class DeleteBookDialog extends Activity
{
	public static final int DELETE_BOOK_AND_FILE = 1;
	public static final int DELETE_BOOK = 2;
	public static final int DELETE_FILE= 3;
	private AbaseDao db = AbaseDao.create();

	@ViewById
	CheckBox cb;
	@ViewById
	TextView tv;
	
	@Extra
	Book book;
	
	@Extra
	BookFile file;

	@Extra
	int MSG;
	
	@AfterViews
	public void init(){
		
		switch (MSG)
		{
		case DELETE_BOOK:
			cb.setVisibility(View.GONE);
			tv.setText(R.string.delete_book);
			break;
			
		case DELETE_FILE:
			cb.setVisibility(View.GONE);
			tv.setText(R.string.delete_file);
			break;
		}
		
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		finish();
		return true;
	}

	@Click(value = { R.id.tv_delete, R.id.tv_cancel })
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.tv_delete:

			switch (MSG)
			{
			case DELETE_BOOK_AND_FILE:
				if (cb.isChecked()) FileUtil.deleteFile(book.path);
				db.delete(book);
				break;
			case DELETE_BOOK:
				db.delete(book);
				break;
			case DELETE_FILE:
				FileUtil.deleteFile(file.path);
				break;
			}
			break;
			
		case R.id.tv_cancel:
			break;
		}
		finish();
	}

}
