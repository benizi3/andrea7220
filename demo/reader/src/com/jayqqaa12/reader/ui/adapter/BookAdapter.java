package com.jayqqaa12.reader.ui.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import com.jayqqaa12.abase.core.adapter.AbaseBaseAdapter;
import com.jayqqaa12.abase.util.sys.SdCardUtil;
import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.engine.BookEngine;
import com.jayqqaa12.reader.model.Book;
import com.jayqqaa12.reader.model.view.BookItemView;
import com.jayqqaa12.reader.model.view.BookItemView_;

@EBean
public class BookAdapter extends AbaseBaseAdapter
{
	@Bean
	BookEngine engine;
	
	
	List<Book> books = new ArrayList<Book>();

	@RootContext
	Context context;

	@Override
	public int getCount()
	{
		return books.size();
	}

	@AfterInject
	public void init()
	{

		Book book = new Book();

		book.name = "book.txt";
		book.path = Environment.getExternalStorageDirectory()+ File.separator+"book.txt";
		book.icon = R.drawable.cover_txt;

		books.add(book);

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
		if (convertView == null)
		{
			view = BookItemView_.build(context);
		}
		else
		{
			view = (BookItemView) convertView;
		}
		view.bind(getItem(position));

		return view;

	}

}
