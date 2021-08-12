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

		if (Validate.isEmpty(data)) return false;
		else if (data.split("\\}\\{").length == 1 && data.endsWith("}") && data.startsWith("{")) return false;
		else if (data.split("\\}\\{").length > 1 && data.endsWith("}") && data.startsWith("{")) return true;
		else return false;
	}

}
