package com.jayqqaa12.abase.util.common;

import com.jayqqaa12.abase.core.AbaseUtil;
import com.jayqqaa12.abase.util.ManageUtil;

import android.content.Context;
import android.widget.Toast;


/**
 * 
 * Toast
 * 
 * @author  jayqqaa12
 *
 */
public class T  extends AbaseUtil
{
	public static void show(String msg ,int time)
	{
		Toast.makeText(getContext(),msg,time).show();
	}
	
	public static void ShortToast(String msg)
	{
		Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
	}
	
	
	public static void LongToast( String msg)
	{
		
		Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
	}
	

	
	
	


}
