package com.wei.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FeedDao extends SQLiteOpenHelper {

	private static final String TAG = FeedDao.class.getSimpleName();
	
	private static final String DB_FILE = "weiYamba.db";
	
	public static final String TABLE_NAME = "feed";
	public static final String ID = "_id";
	public static final String CREATE_TIME = "create_time";
	public static final String CONTENT = "content";
	public static final int VERSION = 2;
	
	public FeedDao(Context context) {
		super(context, DB_FILE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String createTableSql = "create table " + TABLE_NAME +" (_id integer primary key AUTOINCREMENT, " + CREATE_TIME +" text, " + CONTENT +" text)";
		db.execSQL(createTableSql);
		Log.i(TAG, "successfully create the feed table");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists feed");
		onCreate(db);
		Log.i(TAG, "");
	}

}
