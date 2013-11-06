package com.wei.yamba;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.util.Utility;

/**
 * 获取Token
 * @author shi
 */
public class TokenActivity extends Activity {

	private static final String SERVER = "https://api.weibo.com/oauth2/authorize?";
	private static final String APP_KEY = "2446339286";
	private static final String RED_URL = "http://www.sina.com";
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final WebView mWebView = new WebView(this);
		setContentView(mWebView);
		WeiboParameters params = new WeiboParameters();
		params.add("client_id", APP_KEY);
		params.add("redirect_uri", RED_URL);
		params.add("response_type", "token");
		params.add("display", "mobile");
		String url = SERVER + Utility.encodeUrl(params);
		
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WeiboWebViewClient());
		mWebView.setVisibility(View.INVISIBLE);
		mWebView.loadUrl(url);
	}
	
	@SuppressWarnings("rawtypes")
	private void startPostActivity() {
		Bundle options = this.getIntent().getExtras();
		Class activityClass = null;
		try {
			activityClass = Class.forName(options.getString(TokenManager.TOKEN_BEFOR_ACTIVITY));
		} catch (ClassNotFoundException e) {
			Log.e(TokenActivity.class.getSimpleName(), "back to init activity before get token ", e);
		}
		Intent intent = new Intent(this, activityClass);
		startActivity(intent);
	}
	
	class WeiboWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("page redirect", url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i("page start", url);
			if (url.startsWith(RED_URL)) {
				Bundle values = Utility.parseUrl(url);
				String token = values.getString("access_token");
				String expireTime = values.getString("expires_in");
				Toast.makeText(getApplication(), "token=" + token + "; expireTime=" + expireTime, Toast.LENGTH_SHORT).show();
				view.stopLoading();
				TokenManager.putToken(getApplicationContext(), token);
				startPostActivity();
				return;
			} else {				 
				super.onPageStarted(view, url, favicon);
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.i("page finished", url);
			super.onPageFinished(view, url);
			view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Log.e("page error", failingUrl + ", " + errorCode + ", " + description);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
		
	}
	
}
