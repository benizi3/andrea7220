package com.jayqqaa12.abase.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import com.jayqqaa12.abase.util.common.L;
import com.jayqqaa12.abase.util.sys.ApkInfoUtil;
import com.jayqqaa12.abase.util.sys.SysUtil;



/**
 *  继承 这个类 重写  doHandler 来进行 异常 信息的 处理
* @author jayqqaa12 
* @date 2013-6-8
 */
public abstract class AbaseCrashHandler implements UncaughtExceptionHandler
{
	/**
	 * 程序发生异常的时候的方法
	 */
	public void uncaughtException(Thread thread, Throwable ex)
	{
		
		
		StringBuilder sb = new StringBuilder();
		try
		{
			ex.printStackTrace();
			
			sb.append("程序的版本号为" + ApkInfoUtil.getVersionNo());
			sb.append("\n");
			sb.append(SysUtil.getSysInfo());
			sb.append(L.getExceptionStackTrace(ex));
			doHandler(sb.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	/**
	 * 重写 这个类  处理 异常 信息 
	 * 比如  发送给 服务器 
	 * @param msg 
	 */
	public abstract void doHandler(String exceptionMsg);


}
