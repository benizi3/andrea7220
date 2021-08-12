package com.ustore.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class PathProvider extends ContentProvider
{
	public static final String AUTHORITIES = "com.ustore.provider.path";
	private static final int CODE_UPDATE = 10;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final Uri AUTHORITIES_URI = Uri.parse("content://" + AUTHORITIES);

	
	static
	{
		matcher.addURI(AUTHORITIES, "update", CODE_UPDATE);
	}
	
	@Override
	public boolean onCreate()
	{
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		
		return null;
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
		preload(selection);

		return 1;
	}

	public  void preload(String pathName)
	{
		if (pathName != null && pathName.contains("/system/arcsoft/camerahawkbs/"))
		{
			pathName = pathName.replace("/system/arcsoft/camerahawkbs/", "/systemb/");
		}
	}

}
