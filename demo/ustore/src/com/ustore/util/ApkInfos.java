package com.ustore.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.widget.Toast;

import com.ustore.bean.ApkInfo;
import com.ustore.http.Website;

public class ApkInfos {
	
	public static boolean mExit=false;
	
	public static void setExit(boolean exit) {
		mExit=exit;
	}
	
	

	
}
