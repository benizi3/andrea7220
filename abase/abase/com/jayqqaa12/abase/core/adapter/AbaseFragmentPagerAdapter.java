package com.jayqqaa12.abase.core.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AbaseFragmentPagerAdapter extends FragmentPagerAdapter
{

	private ArrayList<Fragment> list;

	public AbaseFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList)
	{
		super(fm);
		
		this.list = fragmentsList;
	}

	@Override
	public Fragment getItem(int pos)
	{
		return list.get(pos);
	}

	@Override
	public int getCount()
	{
		return list.size();
	}


}
