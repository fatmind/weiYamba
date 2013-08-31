package com.wei.yamba;

import com.weiyamba.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingUserInfoActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_user_info);
	}
	
	
	
	
}
