package com.rosstellsweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.rosstellsweather.app.db.RossTellsWeatherDB;
import com.rosstellsweather.app.model.City;
import com.rosstellsweather.app.model.County;
import com.rosstellsweather.app.model.Province;
import com.rosstellsweather.app.util.DataParseUtility;
import com.rosstellsweather.app.util.HttpCallBackListener;
import com.rosstellsweather.app.util.HttpUtil;
import com.rosstellsweather.app.R;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	private static final int LEVEL_PROVINCE = 0;
	private static final int LEVEL_CITY = 1;
	private static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;
	private TextView textView;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private RossTellsWeatherDB rossTellsWeatherDB;
	private List<String> dataList = new ArrayList<String>();

	// ʡ���б�
	private List<Province> provinceList;
	// �����б�
	private List<City> cityList;
	// ���б�
	private List<County> countyList;
	// ѡ�е�ʡ��
	private Province selectedProvince;
	// ѡ�еĳ���
	private City selectedCity;
	// ѡ�е���
	private County selectedCounty;
	// ѡ�еĲ㼶
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		textView = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		Log.d("RossTellsWeather", "׼������adapter");
		listView.setAdapter(adapter);
		Log.d("RossTellsWeather", "Ϊlistview����adapter���");
		rossTellsWeatherDB = RossTellsWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					
					selectedProvince = provinceList.get(position);
					Log.d("RossTellsWeather", "��ȡ"+selectedProvince.getProvinceName()+"�ĳ�����Ϣ");
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					Log.d("RossTellsWeather", "��ȡ�س���Ϣ");
					selectedCity = cityList.get(position);
					queryCounties();
				}

			}

		});
		Log.d("RossTellsWeather", "����ʡ������");
		queryProvinces();
	}

	protected void queryProvinces() {
		Log.d("RossTellsWeather", "123");
		provinceList = rossTellsWeatherDB.loadProvince();
		
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList)
				dataList.add(province.getProvinceName());
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			Log.d("RossTellsWeather", "���ڴӷ�������ȡ����");
			queryFromSever(null, "province");
		}
	}

	protected void queryCities() {
		cityList = rossTellsWeatherDB.loadCity(selectedProvince.getId());
		Log.d("RossTellsWeather", String.valueOf(cityList.size()));
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList)
				dataList.add(city.getCityName());
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			Log.d("RossTellsWeather", "���ڴӷ�������ȡ��������");
			queryFromSever(selectedProvince.getProvinceCode(), "city");
		}

	}

	private void queryCounties() {
		countyList = rossTellsWeatherDB.loadCounty(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList)
				dataList.add(county.getCountyName());
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			Log.d("RossTellsWeather", "���ڴӷ�������ȡ�س�����");
			queryFromSever(selectedCity.getCityCode(), "county");
		}

	}

	private void queryFromSever(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
			Log.d("RossTellsWeather", address);
		} else {
			Log.d("RossTellsWeather", "�������ȡ����ʡ��");
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = DataParseUtility.handleProvinceResponse(
							rossTellsWeatherDB, response);
				} else if ("city".equals(type)) {
					result = DataParseUtility.handleCityResponse(
							rossTellsWeatherDB, response,
							selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = DataParseUtility.handleCountyResponse(
							rossTellsWeatherDB, response, selectedCity.getId());
				}
				if (result) {
					Log.d("RossTellsWeather", "���ڷ�������ѯ������");
					runOnUiThread(new Runnable() {
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();

					}
				});
			}
		});

	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();

	}

	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}

	}
}
