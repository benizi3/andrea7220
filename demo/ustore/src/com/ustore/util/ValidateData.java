package com.ustore.util;

import com.jayqqaa12.abase.util.common.Validate;

public class ValidateData
{

	/***
	 * 判断数据 是否合法
	 * 
	 * @param data
	 */
	public static boolean validate(String data)
	{

		return Validate.notEmpty(data) && !data.contains("<body>")&&data.startsWith("{");
	}

}
