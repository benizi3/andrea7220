package com.jayqqaa12.abase.util.media;

import com.jayqqaa12.abase.util.ManageUtil;

public class KeyguardUtil 
{
	
	/**
	 * 是否 为 锁屏 状态 
	 * 
	 * @return
	 */
	public static boolean isKeyguardRestricted()
	{
		return ManageUtil.getKeyguardManager().inKeyguardRestrictedInputMode();
	}


}
