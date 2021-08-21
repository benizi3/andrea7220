package com.jayqqaa12.reader.ui.adapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;

import com.jayqqaa12.abase.core.AbaseDao;
import com.jayqqaa12.abase.core.adapter.AbaseBaseAdapter;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.Book;
import com.jayqqaa12.reader.ui.adapter.itemview.BookItemView_;

@EBean
public class BookAdapter extends AbaseBaseAdapter<Book>
{

	@RootContext
	Context context;
	
	private AbaseDao db = AbaseDao.create();

	@AfterInject
	public void init()
	{
		data =  db.findAll(Book.class," seq asc ");
		Book book2 = new Book();
		
		book2.icon = "new";
		data.add(book2);
		setItemView(BookItemView_.class, context);
	}


}
