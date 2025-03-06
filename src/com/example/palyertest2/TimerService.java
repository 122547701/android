package com.example.palyertest2;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import android.widget.Toast;

public class TimerService extends Service {

	static Vibrator _vib;
	static Date _StopTime;
	String Url_China = "http://www.weather.com.cn/weather1d/101280101.shtml";
	String LastUpdateTimeKey = "LastUpdateTime";
	// String
	// Url_GD="http://www.gdmo.cn/weather-gdmo/home/home!gdForecast.action";
	String Url_GD = "http://www.nmc.cn/publish/forecast/AGD/guangzhou.html";
	// String
	// url_china="http://flash.weather.com.cn/wmaps/xml/guangdong.xml?75234";
	Handler _Ling_handler = new Handler();
	Runnable _Ling_runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// Ҫ��������
			Check_Ring();

		}
	};
	Timer _timer;

	@SuppressLint("NewApi")
	public void onCreate() {

		super.onCreate();
		Log.v("TimerService", "OnCreate");

		Intent nfIntent = new Intent(this, PlayerTest2MainActivity.class);
//����֪ͨ
		Notification notification = null; // ��ȡ�����õ�Notification
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			Notification.Builder builder = new Notification.Builder(
					this.getApplicationContext()); // ��ȡһ��Notification������
			builder.setContentIntent(
					PendingIntent.getActivity(this, 0, nfIntent, 0))
					.setContentText("����һ��ǰ̨����")
					.setSmallIcon(R.drawable.ic_launcher)
					.setWhen(System.currentTimeMillis());
			notification = builder.build();
		}

		startForeground(1, notification);

		Log.d("MyService", "onCreate executed");

		_timer = new Timer();
		_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.v("wang", "���񻹻���...");
				DoEvent();
				_Ling_handler.postDelayed(_Ling_runnable, 1000);
			}
		}, 3000, 60000);// һ��ʼ�ӳ�3��ִ��TimerTask,�����ӳ�60��ִ��TimerTask,�����ӳ�60��ִ��TimerTask
	}

	public void onStart(Intent intent, int startId) {

		Log.v("TimerService", "onStart");

		// new Thread(_runnable_Http).start();
	}

	private void Check_Ring() {
		java.util.Date DateNow = new java.util.Date();
		int CurrentMin = DateNow.getMinutes();
		int CurrentHour = DateNow.getHours();
		Context ctx = TimerService.this;

		SharedPreferences settings = ctx.getSharedPreferences(
				PlayerTest2MainActivity._PreferenceName, MODE_PRIVATE);
		// ��������
		// SharedPreferences settings=getPreferences(Activity.MODE_PRIVATE);
		String Timer0 = settings.getString(PlayerTest2MainActivity.time0Str,
				"8:5");
		String Timer1 = settings.getString(PlayerTest2MainActivity.time1Str,
				"17:58");

		if ((CurrentHour == Integer.parseInt(Timer0.split(":")[0]) && CurrentMin == Integer
				.parseInt(Timer0.split(":")[1]))
				|| (CurrentHour == Integer.parseInt(Timer1.split(":")[0]) && CurrentMin == Integer
						.parseInt(Timer1.split(":")[1]))) {
			// Play();

			CheckResult1(settings, DateNow);
			CheckResult2(settings, DateNow);
			// _Ling_handler.postDelayed(_Ling_runnable, 59000);
		} else {
			// _Ling_handler.postDelayed(_Ling_runnable, 1000);
		}

	}

	private void DoEvent() {
		java.util.Date DateNow = new java.util.Date();
		int CurrentMin = DateNow.getMinutes();
		int CurrentHour = DateNow.getHours();
		Context ctx = TimerService.this;

		SharedPreferences settings = ctx.getSharedPreferences(
				PlayerTest2MainActivity._PreferenceName, MODE_PRIVATE);
		// ��������
		// SharedPreferences settings=getPreferences(Activity.MODE_PRIVATE);

		String LastUpdateTimeStr = settings.getString(LastUpdateTimeKey, "");
		java.util.Date LastUpdateTime = null;
		if (LastUpdateTimeStr != "") {
			LastUpdateTime = new java.util.Date(LastUpdateTimeStr);
		}

		// ��������
		// SharedPreferences settings=getPreferences(Activity.MODE_PRIVATE);
//		String Timer0 = settings.getString(PlayerTest2MainActivity.time0Str,
//				"8:5");
//		String Timer1 = settings.getString(PlayerTest2MainActivity.time1Str,
//				"17:58");
		// int Interval1 = 60 * Integer.parseInt(Timer0.split(":")[0])
		// + Integer.parseInt(Timer0.split(":")[1]) - 60 * CurrentHour
		// - CurrentMin;
		// int Interval2 = 60 * Integer.parseInt(Timer1.split(":")[0])
		// + Integer.parseInt(Timer1.split(":")[1]) - 60 * CurrentHour
		// - CurrentMin;
		int Interval2 = 10000000;
		if (LastUpdateTime != null) {
			Interval2 = Math.abs(60 * LastUpdateTime.getHours()
					+ LastUpdateTime.getMinutes() - 60 * CurrentHour
					- CurrentMin);
		}
		if (LastUpdateTime == null
				|| LastUpdateTime.getMonth() != DateNow.getMonth()
				|| LastUpdateTime.getDay() != DateNow.getDay()
				|| Math.abs(DateNow.getHours() - LastUpdateTime.getHours()) > 0
				|| Interval2 > 15) {
			Runnable _runnable_Http1 = new Runnable() {
				@Override
				public void run() {

					StartCallRemote(Url_China, handlerGetHttpResult);

				}
			};
			Runnable _runnable_Http2 = new Runnable() {
				@Override
				public void run() {

					StartCallRemote(Url_GD, handlerGetHttpResult);
				}
			};
			new Thread(_runnable_Http1).start();
			new Thread(_runnable_Http2).start();
			// StartCallRemote();
		} else {
			// _handler.postDelayed(_runnable, 30000);
		}
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("LastCheckTime", "���һ�μ��ʱ�䣺" + DateNow.toString());
		editor.commit();
	}

	private void StartCallRemote(String Url, Handler handlerGetHttpResult) {
		//
		// TODO: http request.
		//
		// String Url =
		// "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx/getWeatherbyCityName";
		// String Url = "http://m.weather.com.cn/mweather/101280101.shtml";

		String result = "";
		Boolean[] RequestSuccessed = new Boolean[1];
		for (int i = 0; i < 10; i++) {

			result = HttpRequest.sendPost(Url, "", RequestSuccessed);
			if (RequestSuccessed[0]) {

				break;
			}
		}
		// String result=getWeather("����");
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("url", Url);
		data.putString("result", result);
		data.putBoolean("RequestSuccessed", RequestSuccessed[0]);
		msg.setData(data);
		handlerGetHttpResult.sendMessage(msg);
		// _handler.postDelayed(_runnable, 30000);
	}

	public void onDestroy() {
		_mp.stop();
		_mp.release();
		super.onDestroy();
		Log.v("TimerService", "onDestroy");
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	Handler handlerGetHttpResult = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String Url = data.getString("url");
			String result = data.getString("result");
			Boolean RequestSuccessed = data.getBoolean("RequestSuccessed");
			Context ctx = TimerService.this;
			if (RequestSuccessed) {
				if (Url_China == Url) {
					java.util.Date Date = new java.util.Date();
					FileService.write(result, Date.toString() + ".html",
							"China_Log", false);
					FileService.write(Date.toString() + ":" + result,
							FileService.FILENAME, false);
					
				} else if (Url_GD == Url) {
					java.util.Date Date = new java.util.Date();
					FileService.write(result, Date.toString() + ".html",
							"GD_Log", false);
					FileService.write(Date.toString() + ":" + result,
							FileService.Gd_Weather_FileName, false);
				
				}
			}
			SharedPreferences uiState = ctx.getSharedPreferences(
					PlayerTest2MainActivity._PreferenceName, MODE_PRIVATE);
			// ��������
			java.util.Date Date = new java.util.Date();

			// SharedPreferences uiState=getPreferences(0);
			if (RequestSuccessed) {
				SharedPreferences.Editor editor = uiState.edit();

				editor.putString(LastUpdateTimeKey, Date.toString());
				// editor.putString("Result", Date.toString() + "��ѯ���:" + val);
				editor.commit();
			}

			// CheckResult(val, uiState, Date);
			Log.i("mylog", "������Ϊ-->" + result);
		}

	};

	private void CheckResult1(SharedPreferences uiState, java.util.Date DateNow) {
		/*
		 * Pattern p = Pattern.compile("<li[^>]*>[^<]*</li>"); Matcher m =
		 * p.matcher(val); ArrayList<String> strs = new ArrayList<String>();
		 * 
		 * while (m.find()) { String MatchValue = m.group();
		 * strs.add(MatchValue); }
		 */
		String path = Environment.getExternalStorageDirectory().toString()
				+ File.separator + FileService.DIR_root + File.separator
				+ FileService.FILENAME;
		String val = FileService.read(path);
		String sdateNow_HH = (new SimpleDateFormat("HH")).format(DateNow);
		String sdateNow_Day = (new SimpleDateFormat("d")).format(DateNow);
		String todayWeather = null;
		int dayofWeek = CommonService.getWeekOfDate(DateNow);

		/*
		 * Date date=new Date(); SimpleDateFormat dateFm = new
		 * SimpleDateFormat("EEEE"); String dateFormat= dateFm.format(date);
		 * 
		 * ע����ʽ���ַ����������ִ�Сд
		 * ���ڴ���SimpleDateFormat����Ĳ�����EEEE�������ڣ��硰�����ġ���MMMM���������·ݣ���
		 * ��ʮһ�¡���MM�����·ݣ��硰11���� yyyy������ݣ��硰2010����dd�����죬�硰25��
		 */
		int startindex = 0;
		if (Integer.parseInt(sdateNow_HH) > 12) {
			startindex = val.lastIndexOf(sdateNow_Day + "��ҹ��");
		} else {
			startindex = val.lastIndexOf(sdateNow_Day + "�հ���");
		}
		if (startindex > 0) {
			todayWeather = val.substring(startindex, startindex + 90);
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("d��");
		// String StrDate = sdf.format(Date);

		// for (int i = 0; i < strs.size(); i++)
		// {
		// String todayWeather = strs.get(i);

		if (todayWeather != null) {
			if (todayWeather.contains("��") || todayWeather.contains("ѩ")) {
				String _FilePath = uiState.getString("FilePath", "");
				if (_StopTime == null
						|| !(_StopTime.getDay() == DateNow.getDay()
								&& _StopTime.getHours() == DateNow.getHours() && _StopTime
								.getMinutes() == DateNow.getMinutes()))// �Ѿ�����ֹͣ
				{
					if (dayofWeek == 0 || dayofWeek == 6)// �������ղ���
					{
					} else {
						Play(_FilePath);
						Vibrate();
					}
				}
				// break;
			}
		}
		// }
	}

	private void CheckResult2(SharedPreferences uiState, java.util.Date DateNow) {

		String path = Environment.getExternalStorageDirectory().toString()
				+ File.separator + FileService.DIR_root + File.separator
				+ FileService.Gd_Weather_FileName;
		String val = FileService.read(path);
		int dayofWeek = CommonService.getWeekOfDate(DateNow);
		String todayWeather = null;
		int startindex = 0;

		startindex = val.indexOf("����");

		if (startindex > 0) {
			todayWeather = val.substring(startindex, startindex + 319);
		}

		if (todayWeather != null) {
			if (todayWeather.contains("��") || todayWeather.contains("ѩ")) {
				String _FilePath = uiState.getString("FilePath", "");
				if (_StopTime == null
						|| !(_StopTime.getDay() == DateNow.getDay()
								&& _StopTime.getHours() == DateNow.getHours() && _StopTime
								.getMinutes() == DateNow.getMinutes()))// �Ѿ�����ֹͣ
				{
					if (dayofWeek == 0 || dayofWeek == 6)// �������ղ���
					{
					} else {
						Play(_FilePath);
						Vibrate();
					}
				}
			}
		}

	}

	/*
	 * private void CheckResultForWebservice(String val, SharedPreferences
	 * uiState, java.util.Date Date) { Pattern p =
	 * Pattern.compile("<string[^>]*>[^<]*</string>"); Matcher m =
	 * p.matcher(val); ArrayList<String> strs = new ArrayList<String>();
	 * 
	 * while (m.find()) { String MatchValue = m.group(); strs.add(MatchValue); }
	 * SimpleDateFormat sdf = new SimpleDateFormat("M��d��"); String StrDate =
	 * sdf.format(Date); sdf = new SimpleDateFormat("M��dd��"); String StrDate2 =
	 * sdf.format(Date); for (int i = 0; i < strs.size(); i++) { String
	 * todayWeather = strs.get(i);
	 * 
	 * if (todayWeather.contains(StrDate2) || todayWeather.contains(StrDate)) {
	 * if (todayWeather.contains("С��") || todayWeather.contains("����") ||
	 * todayWeather.contains("����") || todayWeather.contains("����") ||
	 * todayWeather.contains("����")) { String _FilePath =
	 * uiState.getString("FilePath", ""); Play(_FilePath); break; } } } }
	 */
	static MediaPlayer _mp = null;

	private void Play(String filepath) {
		try {
			if (_mp == null || !_mp.isPlaying()) {
				_mp = new MediaPlayer();
				String strPath = filepath;
				File file = new File(strPath);
				FileInputStream fis = new FileInputStream(file);
				_mp.setDataSource(fis.getFD());
				// mp.setDataSource(strPath);
				_mp.prepare();
				_mp.setVolume(1.0F, 1.0F);// �������������������ֱ�������������������������������float����0~1
				_mp.start();
			}
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
			Log.e("play", ex.toString());
		}
	}

	public static void Stop() {
		if (_mp != null) {
			_mp.stop();
			_mp.release();
			_mp = null;
		}
		if (_vib != null) {
			_vib.cancel();
		}
		_StopTime = new java.util.Date();
		// Calendar Calenda= Calendar.getInstance();
		// Calenda.add(Calendar.MINUTE, 1);

	}

	public void Vibrate() {
		_vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		if (_vib != null)
		// _vib.vibrate(milliseconds);
		{
			_vib.vibrate(new long[] { 1000, 2000, 1000, 2000 }, 0);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// WindowManager windowManager = (WindowManager)
		// getSystemService(WINDOW_SERVICE);
		// mRootView = new FloatRootView(this);
		// mRootView.attach(windowManager);
		// mRootView.showBubble();
		// startForeground(1, buildForegroundNotification());
		// return START_STICKY;

		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
	// ����ǰ̨����
	// private Notification buildForegroundNotification() {
	// Notification.Builder builder = new Notification.Builder(this);
	//
	// builder.setOngoing(true);
	//
	// builder.setContentTitle(getString(R.string.notification_title))
	// .setContentText(getString(R.string.notification_content))
	// .setSmallIcon(R.drawable.ic_launcher)
	// .setTicker(getString(R.string.notification_ticker));
	// builder.setPriority(Notification.PRIORITY_MAX);
	// return builder.build();
	// }

}
