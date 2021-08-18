package com.jayqqaa12.reader.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jayqqaa12.abase.core.AbaseDao;
import com.jayqqaa12.abase.core.adapter.AbaseBaseAdapter;
import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.model.Book;
import com.jayqqaa12.reader.model.view.BookItemView;
import com.jayqqaa12.reader.model.view.BookItemView_;

@EBean
public class BookAdapter extends AbaseBaseAdapter
{


	@RootContext
	Context context;
	
	private AbaseDao db = AbaseDao.create();

	List<Book> books = new ArrayList<Book>();
	

	@Override
	public int getCount()
	{
		return books.size();
	}

	@AfterInject
	public void init()
	{
		
		books =  db.findAll(Book.class);

		
		Book book2 = new Book();
		book2.icon = R.drawable.cover_net;
		books.add(book2);

	}

	@Override
	public Book getItem(int position)
	{

		return books.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		BookItemView view;
		if (convertView == null) view = BookItemView_.build(context);
		else view = (BookItemView) convertView;
		
		view.bind(getItem(position));

		return view;

	}

}
