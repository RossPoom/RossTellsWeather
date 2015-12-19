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

	// ���ݿ���
	public static final String DB_NAME = "RossTellsWeather";
	// ���ݿ�汾
	public static final int DB_VERSION = 1;
	private static RossTellsWeatherDB rossTellsWeatherDB;
	private SQLiteDatabase db;

	// ˽�л����췽��
	private RossTellsWeatherDB(Context context) {
		RossTellsWeatherOpenHelper dbHelper = new RossTellsWeatherOpenHelper(
				context, DB_NAME, null, DB_VERSION);
		db=dbHelper.getWritableDatabase();
	}

	// ��ȡRossTellsWeather��ʵ��
	public synchronized static RossTellsWeatherDB getInstance(Context context) {
		if (rossTellsWeatherDB == null) {
			rossTellsWeatherDB = new RossTellsWeatherDB(context);
		}
		Log.d("RossTellsWeather", "���ڷ���DBʵ��");
		return rossTellsWeatherDB;
	}

	// ��Provinceʵ���������ݿ�
	public void saveProvince(Province province) {
		if (province != null) {
			db.execSQL(
					"insert into Province(province_name,province_code) values(?,?)",
					new String[] { province.getProvinceName(),
							province.getProvinceCode() });
		}
	}

	// �����ݿ��ȡȫ�����е�ʡ����Ϣ
	public List<Province> loadProvince() {
		Log.d("RossTellsWeather", "���ڶ�ȡʡ����Ϣ");
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
		Log.d("RossTellsWeather", "��ȡʡ����Ϣ���");
		return list;
	}

	// ��Cityʵ���������ݿ�
	public void saveCity(City city) {
		if (city != null) {
			db.execSQL(
					"insert into City(city_name,city_code,province_id) values(?,?,?)",
					new String[] { city.getCityName(), 
							city.getCityCode(),
							String.valueOf(city.getProvinceId()) });
		}
	}

	// �����ݿ��ȡʡ�������г�����Ϣ
	public List<City> loadCity(int provinceId) {
		Log.d("RossTellsWeather", "���ڶ�ȡ"+provinceId+"������Ϣ");
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
		Log.d("RossTellsWeather", "��ȡ������Ϣ���");
		return list;
	}

	// ��Cityʵ���������ݿ�
	public void saveCounty(County county) {
		if (county != null) {
			db.execSQL(
					"insert into County(county_name,county_code,city_id) values(?,?,?)",
					new String[] { county.getCountyName(), 
							county.getCountyCode(),
							String.valueOf(county.getCityId()) });
		}
	}

	// �����ݿ��ȡ��������������Ϣ
	public List<County> loadCounty(int cityId) {
		Log.d("RossTellsWeather", "���ڶ�ȡ�س���Ϣ");
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
		Log.d("RossTellsWeather", "��ȡ�س���Ϣ���");
		return list;
	}
}
