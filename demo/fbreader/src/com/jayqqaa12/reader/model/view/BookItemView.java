package com.jayqqaa12.reader.model.view;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.geometerplus.android.fbreader.FBReader;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jayqqaa12.abase.util.IntentUtil;
import com.jayqqaa12.abase.util.Txt;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.common.T;
import com.jayqqaa12.filemanage.FileActivity_;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.Book;

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
		
		
		if (book.icon ==R.drawable.cover_net )
		{
			gv_bt.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					IntentUtil.startIntent(getContext(), FileActivity_.class);
				}
			});
		}
		else
		{
			gv_bt.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					IntentUtil.startIntent(getContext(), FBReader.class,new String[] {FBReader.BOOK_KEY,"goto_position"},new Object[]{book.path,true});
				}
			});
		}

	}

}
