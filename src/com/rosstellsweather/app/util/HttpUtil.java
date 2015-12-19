package com.rosstellsweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {
public static void sendHttpRequest(final String address,final HttpCallBackListener listener){
	new Thread(new Runnable() {

		@Override
		public void run() {
			HttpURLConnection connection = null;
			try {
				URL url = new URL(address);
				connection = (HttpURLConnection) url.openConnection();
				Log.d("RossTellsWeather", connection.toString());
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(8000);
				connection.setReadTimeout(8000);
				InputStream in = connection.getInputStream();
				// 读取获得的数据流
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				Log.d("RossTellsWeather", response.toString());
				if(listener!=null)
					//回调listener的onFinish方法
					listener.onFinish(response.toString());
			} catch (Exception e) {
				if(listener!=null)
					//回调listener的onFinish方法
					listener.onError(e);
			} finally {
				if (connection != null)
					connection.disconnect();
			}

		}
	}).start();
}
}
