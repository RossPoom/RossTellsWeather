package com.rosstellsweather.app.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RossTellsWeatherOpenHelper extends SQLiteOpenHelper {

	//province建表语句
	public static final String CREATE_PROVINCE = "create table Province("
			+ "id integer primary key autoincrement,"
			+ "province_name text,"
			+ "province_code text";

	//city建表语句
	public static final String CREATE_City = "create table City("
			+ "id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text"
			+ "province_id integer";
	
	//county建表语句
	public static final String CREATE_County = "create table County("
			+ "id integer primary key autoincrement,"
			+ "country_name text,"
			+ "country_code text"
			+ "city_id integer";
	
	@Override
	//初始化时创建province，city和county三张表
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_City);
		db.execSQL(CREATE_County);

	}

	public RossTellsWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
