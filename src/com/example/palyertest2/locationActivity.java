package com.example.palyertest2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class locationActivity extends Activity {

	public static	LocationManager _lm=null;
	public static Float _MaxDistance=100f,_PerMetro=15f,_PerMilisegundo=15000f;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_layout);

		SharedPreferences settings = getSharedPreferences(Global.SharedPreferencesFileName,MODE_PRIVATE);
		// 存入数据
		// SharedPreferences settings=getPreferences(Activity.MODE_PRIVATE);
//		int SetHour = settings.getInt("Hour", 8);
//		int SetMin = settings.getInt("Minute", 5);
		
//		Float _FilePath = settings.getFloat(etMaxDistanceKey,0.001f);
//		EditText etMaxDistance = (EditText)findViewById (R.id.etMaxDistance);
//		etMaxDistance.setText(Float.toString(_FilePath));
		
		//int[] viewIDs={R.id.etPerMetro,R.id.etPerMilisegundo,R.id.etMaxDistance};
//		Hashtable m1 = new Hashtable(); 
//		 m1.put(R.id.etPerMetro, 15f);
//		 //m1.put(R.id.etPerMilisegundo, 15000);
//		 m1.put(R.id.etMaxDistance, 100f);
//		Enumeration names = m1.keys();
//	      while(names.hasMoreElements()) {
//	    	  String key = String.valueOf( names.nextElement());	     	         
//	         ((EditText)findViewById (Integer.parseInt(key) )).setText(Float.toString(settings.getFloat(key ,(Float) m1.get(key))));
//	      }
	      ((EditText)findViewById (R.id.etPerMetro)).setText(Float.toString(settings.getFloat(String.valueOf(R.id.etPerMetro ) ,15f)));
	      ((EditText)findViewById (R.id.etMaxDistance )).setText(Float.toString(settings.getFloat(String.valueOf(R.id.etMaxDistance ) ,100f)));
	      ((EditText)findViewById (R.id.etPerMilisegundo )).setText(Float.toString(settings.getFloat(String.valueOf(R.id.etPerMilisegundo ),15000f)));
	      
		CheckBox chkDebug=(CheckBox)findViewById(R.id.chkDebug);
		chkDebug.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						CheckBox chkDebug=(CheckBox)arg0;
						Global.inDebug= chkDebug.isChecked()?1:0;
					Global.phoneNumber=	Global.inDebug==1?Global.MyphoneNumber:Global.MyWife;
					
					  SharedPreferences settingsIn= getSharedPreferences(Global.SharedPreferencesFileName,Activity.MODE_PRIVATE);
			    	  	SharedPreferences.Editor editor = settingsIn.edit();
						editor.putInt(String.valueOf(R.id.chkDebug) ,Global.inDebug);
						
						editor.commit();
					}
				});
		chkDebug.setChecked(Global.inDebug==1);
		
		findViewById(R.id.btnReturn).setOnClickListener(new View.OnClickListener() 
	    {   
	      @Override   
	      public void onClick(View v) 
	       {   
  	
	    	  	try
	    	  	{    	  
	    	  	
	    	  	_MaxDistance=Float.parseFloat(((EditText)findViewById (R.id.etMaxDistance)).getText().toString());
	    	  	_PerMetro=Float.parseFloat(((EditText)findViewById (R.id.etPerMetro)).getText().toString());
	    	  	_PerMilisegundo=Float.parseFloat(((EditText)findViewById (R.id.etPerMilisegundo)).getText().toString());
	    	    SharedPreferences settingsIn = getSharedPreferences(Global.SharedPreferencesFileName,
		  				MODE_PRIVATE);
		    	  //SharedPreferences settingsIn= getPreferences(Activity.MODE_PRIVATE);
		    	  	SharedPreferences.Editor editor = settingsIn.edit();
					editor.putFloat(String.valueOf(R.id.etMaxDistance), _MaxDistance);
					editor.putFloat(String.valueOf(R.id.etPerMetro), _PerMetro);
					editor.putFloat(String.valueOf(R.id.etPerMilisegundo), _PerMilisegundo);
					editor.commit();
					
	         //setResult(RESULT_OK,(new Intent()).setAction(_TimeID));   
	         finish();   
	    	  	}catch(Exception ex)
	    	  	{
	    	  		Context ctx = locationActivity.this;
	    	  		Toast.makeText(ctx, ex.toString(), Toast.LENGTH_LONG).show();
	    	  	}
	       }
	    });  
		
		DoEvent(_PerMilisegundo.longValue(),_PerMetro);
	}
	private void DoEvent(long minTime,float minDistance) {
		
		 _lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
		List<String> lp = _lm.getAllProviders();
		for (String item : lp) {
			boolean isProviderEnabled = _lm.isProviderEnabled(item);
			Log.i("8023", "位置服务：" + item + " 状态:" + isProviderEnabled);
			// 位置服务
			if (isProviderEnabled)
			{
				String locationProvider = LocationManager.NETWORK_PROVIDER;
				if (item .equals( locationProvider)) {
					_lm.requestLocationUpdates(locationProvider, minTime, minDistance,
							new NetWork_locationListener());
				} else if (item.equals(LocationManager.GPS_PROVIDER)  ) {
					_lm.requestLocationUpdates("gps", minTime, minDistance,
							new GPS_locationListener());
				}
			}

			Criteria criteria = new Criteria();
			criteria.setCostAllowed(false);
			// 设置位置服务免费
			criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
			// getBestProvider 只有允许访问调用活动的位置供应商将被返回
			// String providerName = lm.getBestProvider(criteria, true);
			String providerName = _lm.getProvider(item).getName();
			Log.i("8023", "------位置服务：" + providerName);

			if (providerName != null) {
				Location location = _lm.getLastKnownLocation(providerName);
				if (location != null) {
					Log.i("8023", "-------" + location);
					// 获取维度信息
					double latitude = location.getLatitude();
					// 获取经度信息
					double longitude = location.getLongitude();
					double Latitude2=23.1429205,longitude2=113.2453986;
					//double Latitude2=23.1383333,longitude2=113.238981667;//cos(23.144)=0.91951993284756013429058407346017
					//double distance1 = CommonService.GetDistance(x, y, point_ZSJNT);
					Double distance2 = CommonService.GetDistance( latitude,longitude, Latitude2,longitude2);
					BigDecimal b0 = new BigDecimal(distance2 );
					BigDecimal f0 = b0.setScale(1, BigDecimal.ROUND_HALF_UP);
					String msg="经度"+longitude+"纬度"+latitude+ "离西场"
							+ (f0) + "(m)";
					String result = providerName + "定位方式： " + providerName
							+ msg;
					if (providerName.contains("gps")) {
						TextView textView = (TextView) findViewById(R.id.tvLocationGPS);
						textView.setText(result);
					} else if(providerName.contains(LocationManager.NETWORK_PROVIDER)){
						TextView textView = (TextView) findViewById(R.id.tvLocationNetWork);
						textView.setText(result);
					}else 
					{
						TextView textView = (TextView) findViewById(R.id.tvLocation3);
						if(textView.getText().toString()!="")
						{
							TextView textView4 = (TextView) findViewById(R.id.tvLocation4);
							textView4.setText(result);
						}
						else
						{
						textView.setText(result);
						}
					}
				} else {

					Toast.makeText(this, providerName + "不可用",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "1.请检查网络连接 \n2.请打开我的位置",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
