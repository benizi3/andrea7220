package com.ustore.download;

import java.sql.SQLData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jayqqaa12.abase.core.Abase;
import com.ustore.bean.DownloadListInfo;

/**
 * 一个业务类
 * 
 */
public class Dao
{

	private static Dao dao = null;
	private Context context;

	private DBHelper dbhelper;
	private SQLiteDatabase database;

	private Dao(Context context)
	{

		this.context = context;

	}

	private DBHelper getDBHelper()
	{
		if (dbhelper == null)
		{
			dbhelper = new DBHelper(context);
		}
		return dbhelper;
	}

	private void getConnection()
	{
		if (database == null || !database.isOpen())
		{
			database = getDBHelper().getWritableDatabase();
		}
	}

	public static Dao getInstance(Context context)
	{
		if (dao == null)
		{
			dao = new Dao(context);
		}
		return dao;
	}
	
	public static Dao getInstance()
	{
		if (dao == null)
		{
			dao = new Dao(Abase.getContext());
		}
		return dao;
	}
	
	

	/**
	 * 是否有对应的apk下载信息
	 */
	public synchronized  boolean notHasInfors(String thread_id)
	{
		// SQLiteDatabase database = getConnection();
		getConnection();
		Cursor cursor = null;
		int count = -1;

		try
		{

			String sql = "select count(*)  from download_info where thread_id=?";
			cursor = database.rawQuery(sql, new String[] { thread_id });

			if (cursor.moveToFirst())
			{
				count = cursor.getInt(0);
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}
			if (cursor != null)
			{
				cursor.close();
			}
		}

		return count == 0;
	}

	/**
	 * 保存 下载的具体信息
	 */
	public synchronized void saveInfo(DownloadInfo info)
	{
		// SQLiteDatabase database = getConnection();
		getConnection();

		try
		{
			ContentValues values = new ContentValues();
			values.put("thread_id", info.getThreadId());
			values.put("start_pos", info.getStartPos());
			values.put("end_pos", info.getEndPos());
			values.put("compelete_size", info.getCompeleteSize());
			values.put("url", info.getUrl());

			database.replace("download_info", null, values);

			ContentValues values2 = new ContentValues();

			values2.put("fileSize", info.getFileSize());
			values2.put("localfile", info.getLocalfile());

			database.update("download_info", values2, "url=?", new String[] { info.getUrl() });

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}
		}

	}

	/**
	 * 得到下载具体信息
	 */
	public synchronized  DownloadInfo getInfo(String thread_id)
	{
		Cursor cursor = null;
		DownloadInfo info = null;
		// SQLiteDatabase database = getConnection();

		getConnection();

		try
		{
			String sql = "select thread_id, start_pos, end_pos,compelete_size,url,fileSize, localfile from download_info where thread_id=?";
			cursor = database.rawQuery(sql, new String[] { thread_id });

			while (cursor.moveToNext())
			{
				info = new DownloadInfo(cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4),
						cursor.getInt(5), cursor.getString(6));
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}
			if (cursor != null)
			{
				cursor.close();

			}
		}

		return info;
	}

	/**
	 * 更新数据库中的下载信息
	 */
	public synchronized void updataInfos(String threadId, int compeleteSize)
	{
		// SQLiteDatabase database = getConnection();

		getConnection();

		try
		{
			String sql = "update download_info set compelete_size=? where thread_id=?";
			Object[] bindArgs = { compeleteSize, threadId };
			database.execSQL(sql, bindArgs);

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}
		}

	}

	/**
	 * 下载完成后删除数据库中的数据
	 */
	public  synchronized void delete(String thread_id)
	{
		// SQLiteDatabase database = getConnection();

		getConnection();

		try
		{
			database.delete("download_info", "thread_id=?", new String[] { thread_id });

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}
		}

	}

	/**
	 * 
	 * 下载列表信息
	 */

	/**
	 * 保存 下载列表的具体信息
	 */
	public synchronized void saveDownloadListInfo(DownloadListInfo info)
	{
		// SQLiteDatabase database = getConnection();
		getConnection();

		try
		{
			ContentValues values = new ContentValues();
			values.put("apk_id", info.getApk_id());
			values.put("name", info.getName());
			values.put("icon", info.getIcon());

			database.replace("download_list", null, values);
			
			ContentValues values2 = new ContentValues();
			values2.put("progress", info.getProgress());

			database.update("download_list", values2, "apk_id=?", new String[] { info.getApk_id() });

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
		}

	}

	/**
	 * 得到下载列表具体信息
	 */
	public  synchronized DownloadListInfo getDownloadListInfo(String apk_id)
	{
		Cursor cursor = null;
		DownloadListInfo info = null;
		getConnection();

		try
		{
			String sql = "select apk_id, name, icon, progress from download_list where apk_id=?";
			cursor = database.rawQuery(sql, new String[] { apk_id });

			while (cursor.moveToNext())
			{
				info = new DownloadListInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}

			if (cursor != null)
			{
				cursor.close();

			}
		}

		return info;
	}

	/**
	 * 更新数据库中的下载列表信息
	 */
	public synchronized void updateDownloadListInfos(String apk_id, String progress)
	{
		// SQLiteDatabase database = getConnection();

		getConnection();

		try
		{
			String sql = "update download_list set progress=? where apk_id=?";
			Object[] bindArgs = { progress, apk_id };
			database.execSQL(sql, bindArgs);

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}
		}

	}

	/**
	 * 下载完成后删除数据库中的下载列表列表数据
	 */
	public synchronized void deleteDownloadList(String apk_id)
	{
		// SQLiteDatabase database = getConnection();

		getConnection();

		try
		{
			database.delete("download_list", "apk_id=?", new String[] { apk_id });

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			if (database != null)
			{
				database.close();
				database = null;
			}
		}

	}

	/**
	 * 查询所有下载列表
	 * 
	 * @return
	 */
	public synchronized  Cursor queryAllDownloadList(SQLiteDatabase db)
	{

		Cursor cursor = null;

		// SQLiteDatabase db=null;

		try
		{

			// db= new DBHelper(context).getReadableDatabase();
//			String sql = "select distinct apk_id, name, icon, progress from download_list ";
//			
//			cursor = database.rawQuery(sql, null);
			
			cursor = db.query("download_list", new String[] { "distinct apk_id", "name", "icon", "progress" }, null, null, null, null, null);
			
			

			// cursor= database.query("download_list", new String[]{"apk_id",
			// "name", "icon", "progress"}, null, null, null, null, null);

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			// if(database!=null) {
			// database.close();
			// database=null;
			// }

			// if(db!=null)
			// db.close();
			//
			// db=null;

		}
		return cursor;

	}

}
