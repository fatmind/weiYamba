package com.wei.yamba;

import android.app.Application;
import android.util.Log;

public class YambaApplication extends Application {

	private final String TAG = YambaApplication.class.getSimpleName();
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "custom application init.");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "application clean up complete.");
	}
	
	

}
