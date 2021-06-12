package com.jayqqaa12.abase.core;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

import com.jayqqaa12.abase.util.ConfigUtil;

/**
 * 
 * 
 * 
 * 如果 不想 每次都 传递 Context 请继承AbaseApp这个类 然后在 清单文件中配置 然后 可以 直接 通过
 * AbaseApp。getContext 获得 application Context 对象 当然 application Context 不能 成为
 * 生命周期 短的 类的 属性 否则 会使那个类 得不到 销毁。。
 * 
 * @author jayqqaa12
 * @date 2013-5-14
 */
public class AbaseEngine
{

	public AbaseEngine()
	{
		Abase.init(this);
	}
	
	protected Context getContext()
	{
		return Abase.getContext();
	}

	protected ContentResolver getResolver()
	{
		return getContext().getContentResolver();
	}

}
