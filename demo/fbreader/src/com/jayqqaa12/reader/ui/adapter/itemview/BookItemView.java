package com.jayqqaa12.reader.ui.adapter.itemview;

import java.io.File;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.geometerplus.android.fbreader.FBReader;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.jayqqaa12.abase.core.adapter.ItemView;
import com.jayqqaa12.abase.util.IntentUtil;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.Book;
import com.jayqqaa12.reader.ui.DeleteBookDialog;
import com.jayqqaa12.reader.ui.DeleteBookDialog_;
import com.jayqqaa12.reader.ui.FileActivity_;

@EViewGroup(R.layout.gv_item)
public class BookItemView extends ItemView<Book>
{
	@ViewById
	Button gv_bt;
	public BookItemView(Context context)
	{
		super(context);
	}
	@AfterInject
	public void init()
	{}
	public void bind(final Book book)
	{
		
		gv_bt.setText(book.name);
		if (book.iconUrl==null) gv_bt.setBackgroundResource(book.getIconRes());
		else ; //TODO 
		if ("new".equals(book.icon))
		{
			gv_bt.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					IntentUtil.startIntent(getContext(), FileActivity_.class);
					((Activity) getContext()).overridePendingTransition(R.anim.push_right_in, R.anim.push_keep);
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

					if (new File(book.path).exists())
					{
						IntentUtil.startIntent(getContext(), FBReader.class, new String[] { FBReader.BOOK_KEY, "goto_position" },
								new Object[] { book.path, true });
					}
					else IntentUtil.startIntent(getContext(), DeleteBookDialog_.class, "book", book, "MSG", DeleteBookDialog.DELETE_BOOK);

				}
			});
			gv_bt.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					IntentUtil.startIntent(getContext(), DeleteBookDialog_.class, "book", book, "MSG",
							DeleteBookDialog.DELETE_BOOK_AND_FILE);
					return true;
				}
			});
		}

	}

}
