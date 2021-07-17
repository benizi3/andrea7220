package com.jayqqaa12.abase.core;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;

import com.jayqqaa12.abase.annotation.view.FindRes;
import com.jayqqaa12.abase.annotation.view.FindRes.ResType;

/**
 * 
 * 
 * 
 * Abase is a not much technical content of the framework of <br>
 * the main application is multiplexed <br>
 * Android some code. at the same time to speed up the development of <br>
 * can achieve activity IOC and simple SQLite ORM <br>
 * ORM afinal <br>
 * reference frame to use Abase inherit AbaseApp <br>
 * If you don't want to inherit from AbaseApp <br>
 * and want to use Abase (tools like) <br>
 * remember in your current application class <br>
 * set Abase.setContext (context) <br>
 * and pass a context to him, <br>
 * if the attribute setScanParent =true<br>
 * scanning the parent class
 * 
 * @author jayqqaa12 (www.jayqqaa12.com)<br>
 * 
 */
public class Abase
{


	private static Context context = null;



	public static Context getContext()
	{
		if (context == null) throw new NullPointerException("Sorry use Abase  must Abase.setContext() ");

		return context;
	}

	public static void setContext(Context context)
	{
		Abase.context = context.getApplicationContext();
	}





	/**
	 * 绑定资源。
	 * 
	 */
	public static void bindAllRes(Object obj, Field field, FindRes res, Resources resources)
	{
		try
		{
			if (res != null)
			{
				ResType type = res.type();

				int id = res.id();
				if (type == ResType.BOOLEAN)
				{
					field.set(obj, resources.getBoolean(id));

				}
				else if (type == ResType.COLOR)
				{
					field.set(obj, resources.getColor(id));
				}
				else if (type == ResType.DRAWABLE)
				{
					field.set(obj, resources.getDrawable(id));
				}
				else if (type == ResType.INT)
				{
					field.set(obj, resources.getInteger(id));
				}
				else if (type == ResType.INT_ARRAY)
				{
					field.set(obj, resources.getIntArray(id));
				}
				else if (type == ResType.STRING)
				{
					field.set(obj, resources.getString(id));
				}
				else if (type == ResType.STRING_ARRAY)
				{
					field.set(obj, resources.getStringArray(id));
				}
				else if (type == ResType.TEXT)
				{
					field.set(obj, resources.getText(id));
				}
				else if (type == ResType.TEXT_ARRAY)
				{
					field.set(obj, ResType.TEXT_ARRAY);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}




}
