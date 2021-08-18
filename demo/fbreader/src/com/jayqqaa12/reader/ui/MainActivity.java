package com.jayqqaa12.reader.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.widget.GridView;

import com.jayqqaa12.reader.R;
import com.jayqqaa12.reader.ui.adapter.BookAdapter;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity
{

	@ViewById
	GridView gv;

	@Bean
	BookAdapter adapter;

	@AfterViews
	public void init()
	{
		gv.setAdapter(adapter);

	}

	@Override
	protected void onStart()
	{
		super.onStart();

		if (adapter != null)
		{
			adapter.init();
			adapter.notifyDataSetChanged();

		}
	}

}
