package com.wei.yamba;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import com.weiyamba.R;

public class TimeLineAcvity extends Activity {

	private FeedDao feedDao;
	
	private SimpleCursorAdapter cursorAdapter;
	private ListView timelineListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		feedDao = new FeedDao(this);
		timelineListView = (ListView)findViewById(R.id.timelineListView);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SQLiteDatabase dbReader = feedDao.getReadableDatabase();
		Cursor cursor = dbReader.query(FeedDao.TABLE_NAME, null, null, null, null, null, FeedDao.ID + " desc", null);
		startManagingCursor(cursor);
		
		/*
		String content = null;
		String postTime = null;
		while(cursor.moveToNext()) {
			content = cursor.getString(cursor.getColumnIndex(FeedDao.CONTENT));
			postTime = cursor.getString(cursor.getColumnIndex(FeedDao.CREATE_TIME));
			timelineView.append(content + "\r\n" + postTime);
		}
		*/
		
		/*
		cursorAdapter = new TimelineCursorAdapter(this, R.layout.timeline_row, cursor);
		timelineListView.setAdapter(cursorAdapter);
		*/
		
		final String[] from = new String[]{FeedDao.ID, FeedDao.CREATE_TIME, FeedDao.CONTENT};
		final int[] to = new int[]{R.id.timeline_num, R.id.timeline_time, R.id.timeline_content};
		cursorAdapter = new SimpleCursorAdapter(this, R.layout.timeline_row, cursor, from, to) ;
		ViewBinder viewBinder = new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if(view.getId() == R.id.timeline_time) {
					String createTime = cursor.getString(cursor.getColumnIndex(FeedDao.CREATE_TIME));
					SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM d HH:mm:ss Z yyyy", Locale.US);
					long time = System.currentTimeMillis();
					try {time = dateFormat.parse(createTime).getTime();} catch (Exception e) {}
					TextView timelineTime = (TextView)view.findViewById(R.id.timeline_time);
					timelineTime.setText(DateUtils.getRelativeTimeSpanString(time));
					return true;
				} else {
					return false;
				}
			}
		};
		cursorAdapter.setViewBinder(viewBinder);
		timelineListView.setAdapter(cursorAdapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		feedDao.close();
	}
	
	
	static class TimelineCursorAdapter extends SimpleCursorAdapter {
		
		static final String[] from = new String[]{FeedDao.ID, FeedDao.CREATE_TIME, FeedDao.CONTENT};
		static final int[] to = new int[]{R.id.timeline_num, R.id.timeline_time, R.id.timeline_content};

		@SuppressWarnings("deprecation")
		public TimelineCursorAdapter(Context context, int layout, Cursor c) {
			super(context, layout, c, from, to);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			super.bindView(view, context, cursor);
			
			String createTime = cursor.getString(cursor.getColumnIndex(FeedDao.CREATE_TIME));
			SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM d HH:mm:ss Z yyyy", Locale.US);
			long time = System.currentTimeMillis();
			try {
				time = dateFormat.parse(createTime).getTime();
			} catch (Exception e) {
			}
			TextView timelineTime = (TextView)view.findViewById(R.id.timeline_time);
			timelineTime.setText(DateUtils.getRelativeTimeSpanString(time));
		}
		
	}
	
}
