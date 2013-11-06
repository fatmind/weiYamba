package com.wei.yamba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class TokenManager {
	
	private static final String PREFERENCE_KEY = "token_preference";
	
	public static final String TOKEN_BEFOR_ACTIVITY = "tokenBeforActivity";
	
	public static void putToken(Context content, String value) {
		SharedPreferences sp = content.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
		sp.edit().putString("token", value).commit();
	}
	
	/**
	 * 若无Token，返回Null
	 * @param context
	 * @return
	 */
	public static String getToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
		return sp.getString("token", null);
 	}
	
	/**
	 * 检查Token是否已存在
	 * @param context
	 * @param activityClass
	 */
	public static void checkLogin(Context context, Class<? extends Activity> activityClass) {
		String token = getToken(context);
		if(token == null) {
			Bundle options = new Bundle();
			options.putString(TOKEN_BEFOR_ACTIVITY, activityClass.getName());
			context.startActivity(new Intent(context, TokenActivity.class));
		}
	}
	
}
