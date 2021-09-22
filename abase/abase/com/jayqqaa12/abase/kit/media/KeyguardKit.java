package com.jayqqaa12.abase.kit.media;

import com.jayqqaa12.abase.kit.ManageKit;

public class KeyguardKit 
{
	
	/**
	 * 是否 为 锁屏 状态 
	 * 
	 * @return
	 */
	public static boolean isKeyguardRestricted()
	{
		return ManageKit.getKeyguardManager().inKeyguardRestrictedInputMode();
	}


}
