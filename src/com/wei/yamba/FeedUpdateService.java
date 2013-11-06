package com.wei.yamba;

import java.io.IOException;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

public class FeedUpdateService extends Service {

	private final String TAG = FeedUpdateService.class.getSimpleName();
	
	private boolean updateServiceRunFlag = false;
	private Updater updater = new Updater();
	private FeedDao feedDao = new FeedDao(this);
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "this binder return null.");
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "service be init created.");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "start servie again.");
		this.updateServiceRunFlag = true;
		new Thread(updater).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "service clean up.");
		this.updateServiceRunFlag = false;
		super.onDestroy();
	}
	
	
	class Updater implements Runnable {
		
		private final String TAG = Updater.class.getSimpleName();
		
		final int page = 1;		// 页码
		final int count = 20;	// 数量

		@Override
		public void run() {
			
			while(updateServiceRunFlag) {
			
				String token = TokenManager.getToken(getApplicationContext());
				final String expireTime = "3600";
				Oauth2AccessToken accessToken = new Oauth2AccessToken(token, expireTime);
				StatusesAPI statusesAPI = new StatusesAPI(accessToken);
				statusesAPI.friendsTimeline(0, 0, count, page, false, WeiboAPI.FEATURE.ORIGINAL, true, new RequestListener() {
					
					@Override
					public void onIOException(IOException arg0) {
						Log.i(TAG, "request wiebo friend timeline exception", arg0);
					}
					
					@Override
					public void onError(WeiboException arg0) {
						Log.i(TAG, "request wiebo friend timeline exception", arg0);
					}
					
					@Override
					public void onComplete(String arg0) {
						
						JSONObject json = JSON.parseObject(arg0);
						String timelineStr = json.getString("statuses");
						JSONArray timeline = JSON.parseArray(timelineStr);
						Object[] timelineList = timeline.toArray();
						
						SQLiteDatabase feeddb = feedDao.getWritableDatabase();
						ContentValues insertValue = new ContentValues();
						for(Object obj : timelineList) {
							JSONObject jsonObj = (JSONObject)obj;
							insertValue.clear();
							insertValue.put(FeedDao.CREATE_TIME, jsonObj.getString("created_at"));
							insertValue.put(FeedDao.CONTENT, jsonObj.getString("text"));
							long id = feeddb.insertOrThrow(FeedDao.TABLE_NAME, null, insertValue);
							Log.i(TAG, "successfully insert, feed id = " + id);
						}
						feeddb.close();
					}
				});
				
				try {
					Thread.sleep(1000 * 5);
				} catch (InterruptedException e) {
					Log.e(TAG, "update friend timeline exception", e);
				}
				
				SQLiteDatabase feeddb = feedDao.getWritableDatabase();
				feeddb.delete(FeedDao.TABLE_NAME, " _id not in (select _id from feed order by _id asc limit " + count + ")", null);
			}
		}
	}
	
}
