package com.rosstellsweather.app.util;

public interface HttpCallBackListener {

	void onError(Exception e);

	void onFinish(String response);
	  
}
