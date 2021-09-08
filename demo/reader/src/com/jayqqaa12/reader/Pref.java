package com.jayqqaa12.reader;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref
public interface Pref
{

	@DefaultInt(25)
	int font();
	
	@DefaultInt(0)
	int progress();
	

}
