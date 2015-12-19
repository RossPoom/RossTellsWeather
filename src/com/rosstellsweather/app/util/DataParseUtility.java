package com.rosstellsweather.app.util;


import android.text.TextUtils;
import android.util.Log;

import com.rosstellsweather.app.db.RossTellsWeatherDB;
import com.rosstellsweather.app.model.City;
import com.rosstellsweather.app.model.County;
import com.rosstellsweather.app.model.Province;

public class DataParseUtility {
	//处理省级数据
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
	
	//处理市级数据
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
	
	//处理县级数据
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
}
