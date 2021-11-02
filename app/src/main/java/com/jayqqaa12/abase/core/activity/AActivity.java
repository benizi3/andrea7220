package com.jayqqaa12.abase.core.activity;

import android.app.Activity;
import android.os.Bundle;


public class AActivity extends Activity
{

	

	protected void afterInject(){
		
	}
 
	
	protected void afterView()
	{
		init();
		connect();
	}
	
	protected void init(){
	}
	
	/***
	 * 填充数据 连接网络等
	 */
	protected void connect()
	{
	}

	

	

}
