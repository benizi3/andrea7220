package com.jayqqaa12.abase.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayqqaa12.abase.annotation.view.FindEngine;
import com.jayqqaa12.abase.annotation.view.FindRes;
import com.jayqqaa12.abase.annotation.view.FindRes.ResType;
import com.jayqqaa12.abase.annotation.view.FindView;
import com.jayqqaa12.abase.exception.AbaseException;
import com.jayqqaa12.abase.util.common.L;

/**
 * 
 * 
 * <br>
 * Abase 是一个 没有 多少技术含量的 框架 <br>
 * 主要 用途 在于 android 的一些 代码的 复用 <br>
 * 同时加快 开发 <br>
 * 可以 实现 activity 的ioc 简单 的 sqlite orm 操作 和 网络图片的 异步加载 及其缓存 <br>
 * orm 与 bitmap 部分 参考 afinal框架（www.afinal.org） 版权 归属 原作者<br>
 * 要使用 Abase 请继承 AbaseApp <br>
 * 如果不想 继承 AbaseApp <br>
 * 又想 使用 Abase （工具类也一样） <br>
 * 记得 在你 当前的 application 类 里面 <br>
 * 设置 Abase.setContext(context) <br>
 * 并且 传递 一个 context 给 他， <br>
 * 如果 要 扫描 父类的 属性 setScanParent =true<br>
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

	private static boolean openActivtiy = true;
	private static boolean openEngine = true;
	private static boolean openRes = true;
	
	private static FindRes findRes = null;
	private static FindEngine findEngine = null;
	
	private static Context context = null;
	private static boolean scanParent = false;

	// 缓存 找到的 parentView 数据
	private static Map<Integer, View> parentViews = new HashMap<Integer, View>();

	private static Map<Integer, View> pageViews = new TreeMap<Integer, View>();

	public static void cleanCache()
	{
		parentViews.clear();
		pageViews.clear();
	}

	public static Context getContext()
	{
		if (context == null) throw new NullPointerException("Sorry use Abase  must Abase.setContext() ");

		return context;
	}

	public static void setContext(Context context)
	{
		Abase.context = context.getApplicationContext();
	}

	public static void setScanParent(boolean scanParent)
	{
		Abase.scanParent = scanParent;
	}

	public static boolean isOpenEngine()
	{
		return openEngine;
	}

	public static void setOpenEngine(boolean openEngine)
	{
		Abase.openEngine = openEngine;
	}

	public static boolean isOpenRes()
	{
		return openRes;
	}

	public static void setOpenRes(boolean openRes)
	{
		Abase.openRes = openRes;
	}

	public static boolean isOpenActivtiy()
	{
		return openActivtiy;
	}

	public static void setOpenActivtiy(boolean openActivtiy)
	{
		Abase.openActivtiy = openActivtiy;
	}

	public static boolean isOpenService()
	{
		return openEngine;
	}

	public static void setOpenService(boolean openService)
	{
		Abase.openEngine = openService;
	}

	public static void init(Object obj)
	{
		findEngine = null;
		findRes = null;
		Resources resources = null;
		if (getContext() != null) resources = getContext().getResources();

		Field[] fields = getFields(obj);

		if (fields == null || fields.length < 1) return;

		for (Field field : fields)
		{
			try
			{
				if (openEngine)
				{
					findEngine = field.getAnnotation(FindEngine.class);
					if (findEngine != null)
					{
						Class clazz = field.getType();
						field.setAccessible(true);

						field.set(obj, Engine.getInstance(clazz));

						continue;
					}

				}

				if (openRes)
				{
					findRes = field.getAnnotation(FindRes.class);

					if (findRes != null)
					{
						field.setAccessible(true);
						bindAllRes(obj, field, findRes, resources);

						continue;
					}
				}

			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * 获得 fields
	 * 
	 * @param obj
	 * @return
	 */
	public static Field[] getFields(Object obj)
	{
		Field[] f1 = obj.getClass().getDeclaredFields();

		if (scanParent)
		{
			Field[] f2 = obj.getClass().getSuperclass().getDeclaredFields();
			Field[] f3 = new Field[f1.length + f2.length];
			System.arraycopy(f1, 0, f3, 0, f1.length);
			System.arraycopy(f2, 0, f3, f1.length, f2.length);

			return f3;
		}
		else
		{

			return f1;
		}

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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void initViewForReflect(Activity obj)
	{
		initViewForReflect(obj, obj.getWindow().getDecorView());
	}

	public static void initViewForReflect(Activity obj,View rootView)
	{
		Resources resources = obj.getResources();
		if (!Abase.isOpenActivtiy()) return;

		Field[] fields = Abase.getFields(obj);

		if (fields == null || fields.length < 1) return;

		for (Field field : fields)
		{
			FindView find = field.getAnnotation(FindView.class);
			
			if (find == null) continue;
			
			View view = null;
			try
			{
				field.setAccessible(true);
				int id = find.id();
				int parId = find.parId();
				int pageId = find.pageId();
				int pageNum = find.pageNum();

				if (parId > 0)
				{

					if (parentViews.get(parId) != null) view = parentViews.get(parId);
					else
					{
						view = rootView.findViewById(parId);
						if (view != null) parentViews.put(parId, view);
					}
					if (view != null)
					{
						if (id != 0) field.set(obj, view.findViewById(id));
						else field.set(obj, view);
					}
					view = null;
				}
				else if (pageId > 0 && pageViews != null)
				{
					if (pageViews.get(pageNum) != null) view = pageViews.get(pageNum);
					else
					{
						//FIXME 可能出问题的地方 
						view = obj.getLayoutInflater().inflate(pageId, null);
						if (view != null) pageViews.put(pageNum, view);
					}
					if (view != null)
					{
						if (id != 0) field.set(obj, view.findViewById(id));
						else field.set(obj, view);
					}
					view = null;
				}
				else
				{
					field.set(obj, rootView.findViewById(id));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return;
			}

			// setting text
			int textId = find.textId();
			if (textId > 0) setViewText(resources, field, textId, obj);

			int imageId = find.imageId();
			if (imageId > 0) setViewImage(resources, field, imageId, obj);

			String tag = find.tag();
			if (!TextUtils.isEmpty(tag)) setViewTag(field, tag, obj);

			boolean clickBind = find.click();
			if (clickBind) setClickListener(field, obj);

			boolean longClickBind = find.longClick();
			if ((longClickBind)) setLongClickListener(field, obj);

			boolean itemClickBind = find.itemClick();
			if ((itemClickBind)) setItemClickListener(field, obj);

			boolean itemLongClickBind = find.itemLongClick();
			if ((itemLongClickBind)) setItemLongClickListener(field, obj);

			boolean select = find.itemClick();
			if (select) setItemSelectListener(field, obj);

			boolean checkedChangeBind = find.checkedChange();
			if ((checkedChangeBind)) setCheckedChangeListener(field, obj);

			boolean conextMenuBind = find.contextMenu();
			if ((conextMenuBind)) setContextMenuBind(field, obj);

			boolean scroll = find.scroll();
			if (scroll) setScrollListener(field, obj);

			boolean touchBind = find.touch();
			if ((touchBind)) setTouchListener(field, obj);

			boolean textWatcherBind = find.textChanged();
			if ((textWatcherBind)) setTextWatcherListener(field, obj);

			boolean focusChangeBind = find.focusChange();
			if ((focusChangeBind)) setFocusChangeListener(field, obj);

			boolean gestureBind = find.gesture();
			if ((gestureBind)) setGestureListener(field, obj);

			boolean childClickBind = find.childClick();
			if (childClickBind) setChildClick(field, obj);
			boolean pagerChangeBind = find.pagerChange();
			if (pagerChangeBind) setPagerChangeClick(field, obj);

		}
	}

	private static void setPagerChangeClick(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof ViewPager)
			{
				((ViewPager) obj).setOnPageChangeListener((OnPageChangeListener)srcobj);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void setChildClick(Field field, Object srcobj)
	{

		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof ExpandableListView)
			{
				((ExpandableListView) obj).setOnChildClickListener((OnChildClickListener)srcobj);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void setGestureListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof View)
			{
				((View) obj).setOnTouchListener((OnTouchListener)srcobj);
				((View) obj).setOnLongClickListener((OnLongClickListener)srcobj);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void setFocusChangeListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof View)
			{
				((View) obj).setOnFocusChangeListener((OnFocusChangeListener)srcobj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void setTextWatcherListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof TextView)
			{
				((TextView) obj).addTextChangedListener((TextWatcher)srcobj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void setTouchListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof View)
			{
				((View) obj).setOnTouchListener((OnTouchListener)srcobj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void setScrollListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof AbsListView)
			{
				((AbsListView) obj).setOnScrollListener((OnScrollListener)srcobj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void setViewTag(Field field, String tag, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof View)
			{
				((View) obj).setTag(tag);
			}

		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	public static void setViewImage(Resources resources, Field field, int imageId, Object srcobj)
	{

		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof ImageView)
			{
				((ImageView) obj).setImageResource(imageId);

			}

		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	public static void setViewText(Resources resources, Field field, int textId, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof TextView)
			{
				((TextView) obj).setText(resources.getString(textId));

			}

		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	public static void setContextMenuBind(Field field, Activity activity)
	{
		try
		{
			Object obj = field.get(activity);
			if (obj instanceof View)
			{
				(activity).registerForContextMenu(((View) obj));

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void setCheckedChangeListener(Field field, Object srcobj)
	{

		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof CompoundButton)
			{
				((CompoundButton) obj).setOnCheckedChangeListener((OnCheckedChangeListener)srcobj);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void setClickListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof View)
			{

				((View) obj).setOnClickListener((OnClickListener)srcobj);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void setLongClickListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof View)
			{
				((View) obj).setOnLongClickListener((OnLongClickListener)srcobj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void setItemClickListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof AbsListView)
			{
				((AbsListView) obj).setOnItemClickListener((OnItemClickListener)srcobj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void setItemLongClickListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof AbsListView)
			{
				((AbsListView) obj).setOnItemLongClickListener((OnItemLongClickListener)srcobj);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void setItemSelectListener(Field field, Object srcobj)
	{
		try
		{
			Object obj = field.get(srcobj);
			if (obj instanceof View)
			{
				((AbsListView) obj).setOnItemSelectedListener((OnItemSelectedListener)srcobj);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
