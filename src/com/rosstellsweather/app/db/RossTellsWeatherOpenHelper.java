package com.rosstellsweather.app.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RossTellsWeatherOpenHelper extends SQLiteOpenHelper {

	//province�������
	public static final String CREATE_PROVINCE = "create table Province("
			+ "id integer primary key autoincrement,"
			+ "province_name text,"
			+ "province_code text)";

	//city�������
	public static final String CREATE_City = "create table City("
			+ "id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text,"
			+ "province_id integer)";
	
	//county�������
	public static final String CREATE_County = "create table County("
			+ "id integer primary key autoincrement,"
			+ "county_name text,"
			+ "county_code text,"
			+ "city_id integer)";
	
	@Override
	//��ʼ��ʱ����province��city��county���ű�
	public void onCreate(SQLiteDatabase db) {
		Log.d("RossTellsWeather","��һ��ʹ�ã������ִ���SQLiteOpenHelper...");
		db.execSQL(CREATE_PROVINCE);
		Log.d("RossTellsWeather", "�״�ʹ�ã����ڴ���Province��");
		db.execSQL(CREATE_City);
		Log.d("RossTellsWeather", "�״�ʹ�ã����ڴ���City��");
		db.execSQL(CREATE_County);
		Log.d("RossTellsWeather", "�״�ʹ�ã����ڴ���County��");

	}

	public RossTellsWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		
		super(context, name, factory, version);
		Log.d("RossTellsWeather","��һ��ʹ�ã����ڴ���SQLiteOpenHelper...");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
