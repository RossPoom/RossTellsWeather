package com.rosstellsweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.rosstellsweather.app.model.City;
import com.rosstellsweather.app.model.County;
import com.rosstellsweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.text.StaticLayout;

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
	}

	// ��ȡRossTellsWeather��ʵ��
	public synchronized static RossTellsWeatherDB getInstance(Context context) {
		if (rossTellsWeatherDB == null) {
			rossTellsWeatherDB = new RossTellsWeatherDB(context);
		}
		return rossTellsWeatherDB;
	}

	// ��Provinceʵ���������ݿ�
	public void saveProvince(Province province) {
		if (province != null) {
			db.execSQL(
					"insert into Province(id,provinceName,provinceCode values(?,?,?)",
					new String[] { String.valueOf(province.getId()),
							province.getProvinceName(),
							province.getProvinceCode() });
		}
	}

	// �����ݿ��ȡȫ�����е�ʡ����Ϣ
	public List<Province> loadProvince() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.rawQuery("select * from Province", null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("ProvinceName")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("ProvinceCode")));
				list.add(province);
			} while (cursor.moveToNext());

		}
		if (cursor != null)
			cursor.close();
		return list;
	}

	// ��Cityʵ���������ݿ�
	public void saveCity(City city) {
		if (city != null) {
			db.execSQL(
					"insert into City(id,cityName,cityCode values(?,?,?,?)",
					new String[] { String.valueOf(city.getId()),
							city.getCityName(), city.getCityCode(),
							String.valueOf(city.getProvinceId()) });
		}
	}

	// �����ݿ��ȡʡ�������г�����Ϣ
	public List<City> loadCity(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db
				.rawQuery(
						"select * from City where cityId="
								+ String.valueOf(provinceId), null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName((cursor.getString(cursor
						.getColumnIndex("CityName"))));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("CityCode")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
		return list;
	}

	// ��Cityʵ���������ݿ�
	public void saveCounty(County county) {
		if (county != null) {
			db.execSQL(
					"insert into County(id,countyName,countyCode,cityId values(?,?,?,?)",
					new String[] { String.valueOf(county.getId()),
							county.getCountyName(), 
							county.getCountyCode(),
							String.valueOf(county.getCityId()) });
		}
	}

	// �����ݿ��ȡ��������������Ϣ
	public List<County> loadCounty(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.rawQuery("select * from County where cityId="
				+ String.valueOf(cityId), null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName((cursor.getString(cursor
						.getColumnIndex("CountyName"))));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("CountyCode")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
		return list;
	}
}
