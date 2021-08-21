

package com.jayqqaa12.reader;

import org.geometerplus.zlibrary.core.sqliteconfig.ZLSQLiteConfig;
import org.geometerplus.zlibrary.ui.android.image.ZLAndroidImageManager;
import org.geometerplus.zlibrary.ui.android.library.ZLAndroidLibrary;

import com.jayqqaa12.abase.core.AbaseApp;

public  class App extends AbaseApp {
	@Override
	public void onCreate() {
		super.onCreate();

		new ZLSQLiteConfig(this);
		new ZLAndroidImageManager();
		new ZLAndroidLibrary(this);
	}
}
