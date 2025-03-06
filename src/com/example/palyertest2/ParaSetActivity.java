package com.example.palyertest2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class ParaSetActivity extends Activity {
	public static String _PreferenceName = "VIDEO";
	String _TimeID;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	//TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_paraset);
	
	findViewById(R.id.btnReturn).setOnClickListener(new View.OnClickListener() 
    {   
      @Override   
      public void onClick(View v) 
       {   
         setResult(RESULT_OK,(new Intent()).setAction(_TimeID));   
         finish();   
       }   
    });  
//  得到跳转到该Activity的Intent对象
    Intent intent = getIntent();
    String[] SelectedValue= intent.getStringExtra("SelectedValue").split("#");
    _TimeID=SelectedValue[0];
	TimePicker m_TimePicker = (TimePicker) findViewById(R.id.timePicker1);
	// 设置为24小时制显示
	m_TimePicker.setIs24HourView(true);
	m_TimePicker.setCurrentHour(Integer.parseInt(SelectedValue[1].split(":")[0]));
	m_TimePicker.setCurrentMinute(Integer.parseInt(SelectedValue[1].split(":")[1]) );

	// 监听时间改变
	m_TimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() 
	{
				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay,int minute) {
					Context ctx = ParaSetActivity.this;

					SharedPreferences uiState = ctx.getSharedPreferences(
							_PreferenceName, MODE_PRIVATE);
					// SharedPreferences uiState=getPreferences(0);
					SharedPreferences.Editor editor = uiState.edit();
					
					editor.putString(_TimeID, hourOfDay+":"+minute);
					
					editor.commit();

					// 时间改变时处理
					// c.set(year, month, day, hourOfDay, minute, second);
				}
	
	 });
	}
}
