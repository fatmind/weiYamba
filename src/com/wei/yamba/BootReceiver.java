package com.wei.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		/*
		 * 1. start feed update service
		 */
		context.startService(new Intent(context, FeedUpdateService.class));
		Log.d(BootReceiver.class.getSimpleName(), "feed update service starting ...");
		
		/*
		 * 2. check token
		 */
		TokenManager.checkLogin(context, TimeLineAcvity.class);
	}

}
