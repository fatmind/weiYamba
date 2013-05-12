package com.wei.yamba;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
	
	private static final String PREFERENCE_KEY = "token_preference";
	
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
	
}
