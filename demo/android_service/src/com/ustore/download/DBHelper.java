package com.ustore.download;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context) {
		super(context, "download.db", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//断点续传记录下载信息
		db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id TEXT NOT NULL, "
				+ "start_pos integer, end_pos integer, compelete_size integer,url char,fileSize integer,localfile varchar(20));");
		
//		未完成的下载表信息
		db.execSQL("create table download_list(_id integer PRIMARY KEY AUTOINCREMENT,  apk_id TEXT   NOT NULL, " 
				+"name text, icon text, progress text);");
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS "+"download_info");
		db.execSQL("DROP TABLE IF EXISTS "+"download_list");
		onCreate(db);
	}

}
