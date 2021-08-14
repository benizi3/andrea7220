package com.jayqqaa12.reader.model.view;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.Book;
import com.jayqqaa12.reader.ui.BookActivity_;

@EViewGroup(R.layout.gv_item)
public class BookItemView extends LinearLayout
{

	@ViewById
	Button gv_bt;

	public BookItemView(Context context)
	{
		super(context);
	}

	@AfterInject
	public void init()
	{

	}

	public void bind(final Book book)
	{
		
		gv_bt.setText(book.name);
		gv_bt.setBackgroundResource(book.icon);
		
		gv_bt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = BookActivity_.intent(getContext()).book(book).get();
				getContext().startActivity(intent);
			}
		});
		
		
		
		
		
		
	}

}
