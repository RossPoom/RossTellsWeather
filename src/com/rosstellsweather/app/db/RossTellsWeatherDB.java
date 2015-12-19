package com.rosstellsweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.rosstellsweather.app.model.City;
import com.rosstellsweather.app.model.County;
import com.rosstellsweather.app.model.Province;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

public class RossTellsWeatherDB {

	// 数据库名
	public static final String DB_NAME = "RossTellsWeather";
	// 数据库版本
	public static final int DB_VERSION = 1;
	private static RossTellsWeatherDB rossTellsWeatherDB;
	private SQLiteDatabase db;

	// 私有化构造方法
	private RossTellsWeatherDB(Context context) {
		RossTellsWeatherOpenHelper dbHelper = new RossTellsWeatherOpenHelper(
				context, DB_NAME, null, DB_VERSION);
		db=dbHelper.getWritableDatabase();
	}

	// 获取RossTellsWeather的实例
	public synchronized static RossTellsWeatherDB getInstance(Context context) {
		if (rossTellsWeatherDB == null) {
			rossTellsWeatherDB = new RossTellsWeatherDB(context);
		}
		Log.d("RossTellsWeather", "正在返回DB实例");
		return rossTellsWeatherDB;
	}

	// 将Province实例存入数据库
	public void saveProvince(Province province) {
		if (province != null) {
			db.execSQL(
					"insert into Province(province_name,province_code) values(?,?)",
					new String[] { province.getProvinceName(),
							province.getProvinceCode() });
		}
	}

	// 从数据库读取全国所有的省份信息
	public List<Province> loadProvince() {
		Log.d("RossTellsWeather", "正在读取省份信息");
		List<Province> list = new ArrayList<Province>();
//		Cursor cursor = db.rawQuery("select * from Province", null);
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				Log.d("RossTellsWeather", province.getProvinceCode()+province.getProvinceName());
				list.add(province);
			} while (cursor.moveToNext());

		}
		if (cursor != null)
			cursor.close();
		Log.d("RossTellsWeather", "读取省份信息完成");
		return list;
	}

	// 将City实例存入数据库
	public void saveCity(City city) {
		if (city != null) {
			db.execSQL(
					"insert into City(city_name,city_code,province_id) values(?,?,?)",
					new String[] { city.getCityName(), 
							city.getCityCode(),
							String.valueOf(city.getProvinceId()) });
		}
	}

	// 从数据库读取省份下所有城市信息
	public List<City> loadCity(int provinceId) {
		Log.d("RossTellsWeather", "正在读取"+provinceId+"城市信息");
		List<City> list = new ArrayList<City>();
		Cursor cursor = db
				.rawQuery(
						"select * from City where province_id="
								+ String.valueOf(provinceId), null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName((cursor.getString(cursor
						.getColumnIndex("city_name"))));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
		Log.d("RossTellsWeather", "读取城市信息完成");
		return list;
	}

	// 将City实例存入数据库
	public void saveCounty(County county) {
		if (county != null) {
			db.execSQL(
					"insert into County(county_name,county_code,city_id) values(?,?,?)",
					new String[] { county.getCountyName(), 
							county.getCountyCode(),
							String.valueOf(county.getCityId()) });
		}
	}

	// 从数据库读取城市下所有县信息
	public List<County> loadCounty(int cityId) {
		Log.d("RossTellsWeather", "正在读取县城信息");
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.rawQuery("select * from County where city_id="
				+ String.valueOf(cityId), null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName((cursor.getString(cursor
						.getColumnIndex("county_name"))));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCityId(cityId);
				Log.d("RossTellsWeather", county.getCountyCode()+county.getCountyName());
				list.add(county);
			} while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
		Log.d("RossTellsWeather", "读取县城信息完成");
		return list;
	}
}
