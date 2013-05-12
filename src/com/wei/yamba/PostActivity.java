package com.wei.yamba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weiyamba.R;

public class PostActivity extends Activity {

	private final String POST_URL = "https://api.weibo.com/2/statuses/update.json";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post);
		
		Button commit = (Button)findViewById(R.id.post_button);
		commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView content = (TextView)findViewById(R.id.post_content);
				String postStatus = content.getText().toString();
				String token = TokenManager.getToken(getApplicationContext());
				Map<String, String> params = new HashMap<String, String>();
				params.put("access_token", token);
				params.put("status", postStatus);
				/*
				 * async task to execute
				 */
				PostTask postTask = new PostTask();
				postTask.execute(POST_URL, params);
			}
		});
	}
	
	class PostTask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			String url = (String)params[0];
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Map<String, String> postParams = (Map)params[1];
			try {
				return post(url, postParams);
			} catch (Exception e) {
				Log.e("PostTask", "post status exception", e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		}
	}
	
	private String post(String url, Map<String, String> params) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Entry<String, String> e : params.entrySet()) {
			nvps.add(new BasicNameValuePair(e.getKey(), e.getValue()));  
		}
		httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		HttpResponse response = httpclient.execute(httppost);
		int respCode = response.getStatusLine().getStatusCode();
		String respContent = EntityUtils.toString(response.getEntity());
		Log.w("PostTask", "response code = " + respCode + ", respons content : " + respContent);
		
		httpclient.getConnectionManager().shutdown();
		
		return respContent;
	}

}
