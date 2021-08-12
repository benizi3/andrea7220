package com.ustore.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static final String DB_NAME="ustore.db";
	
	private static final String TB_FAV="create table apkversion(_id INTEGER PRIMARY KEY ," +
			"apk_id TEXT UNIQUE NOT NULL, version TEXT NOT NULL);";
	
	
	SQLiteDatabase db;
	
	public Database(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
		this.db=getWritableDatabase();
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TB_FAV);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+"apkversion");
		onCreate(db);
	}
	
	/**
	 * 插入apkversion
	 * @param article_id
	 * @param title
	 * @param date
	 * @param content
	 */
	public void insertApkVersion(String apk_id, String version) {
		String sql="insert into apkversion(apk_id, version)values(?,?);";
		Object[] bindArgs={apk_id, version};
		db.execSQL(sql, bindArgs);
	}
	
	/**
	 * 更新apkversion数据
	 * @param apk_id
	 * @param version
	 */
	public void updateApkVersion(String apk_id, String version) {
		ContentValues cv=new ContentValues();
		cv.put("version", version);
		db.update("apkversion", cv, "apk_id='"+apk_id+"'", null);
	}
	
	/**
	 * 根据apk_id删除apkversion数据
	 * @param article_id
	 */
	public void deleteApkVersion(String apk_id) {
		db.delete("apkversion", "apk_id='"+apk_id+"'", null);
	}
	
	/**
	 * 根据apk_id查询apkversion数据
	 * @param article_id
	 */
	public Cursor queryApkVersion(String apk_id) {
		return db.query("apkversion", new String[]{"version"}, "apk_id='"+apk_id+"'", null, null, null, null);
		
	}

}
