package com.rosstellsweather.app.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.rosstellsweather.app.db.RossTellsWeatherDB;
import com.rosstellsweather.app.model.City;
import com.rosstellsweather.app.model.County;
import com.rosstellsweather.app.model.Province;

public class DataParseUtility {
	//处理服务器回传的省级数据
	public synchronized static boolean handleProvinceResponse(
			RossTellsWeatherDB rossTellsWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			Log.d("RossTellsWeather", "正在解析服务器返回的省份信息");
			String[] allProvinces = response.split(",");
			Log.d("RossTellsWeather", allProvinces.toString());
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					Log.d("RossTellsWeather", province.getProvinceCode());
					province.setProvinceName(array[1]);
					Log.d("RossTellsWeather", province.getProvinceName());
					rossTellsWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	//处理服务器回传的市级数据
	public synchronized static boolean handleCityResponse(
			RossTellsWeatherDB rossTellsWeatherDB, String response,int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String p : allCities) {
					String[] array = p.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					rossTellsWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	//处理服务器回传的县级数据
		public synchronized static boolean handleCountyResponse(
				RossTellsWeatherDB rossTellsWeatherDB, String response,int cityId) {
			if (!TextUtils.isEmpty(response)) {
				String[] allCountys = response.split(",");
				if (allCountys != null && allCountys.length > 0) {
					for (String p : allCountys) {
						String[] array = p.split("\\|");
						County county = new County();
						county.setCountyCode(array[0]);
						county.setCountyName(array[1]);
						county.setCityId(cityId);
						Log.d("RossTellsWeather", county.getCountyCode()+county.getCountyName());
						rossTellsWeatherDB.saveCounty(county);
					}
					return true;
				}
			}
			return false;
		}
		
		//处理服务器回传的天气JSON数据
		public static void handleWeatherResponse(Context context,String response){
			try {
				JSONObject jsonObject=new JSONObject(response);
				JSONObject weatherinfo=jsonObject.getJSONObject("weatherinfo");
				String cityName=weatherinfo.getString("city");
				String weatherCode=weatherinfo.getString("cityid");
				String temp1=weatherinfo.getString("temp1");
				String temp2=weatherinfo.getString("temp2");
				String weatherDesp=weatherinfo.getString("weather");
				String publishTime=weatherinfo.getString("ptime");
				saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		//将服务器返回的所有天气信息存储到SharedPreferences文件中
		private static void saveWeatherInfo(Context context, String cityName,
				String weatherCode, String temp1, String temp2,
				String weatherDesp, String publishTime) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月D日",Locale.CHINA);
			SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putBoolean("city_selected", true);
			editor.putString("city_name", cityName);
			editor.putString("weatherCode", weatherCode);
			editor.putString("temp1", temp1);
			editor.putString("temp2", temp2);
			editor.putString("weather_desp", weatherDesp);
			editor.putString("publish_time", publishTime);
			editor.putString("current_date", sdf.format(new Date()));
			editor.commit();
			
		}

}
