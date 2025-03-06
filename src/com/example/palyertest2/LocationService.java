package com.example.palyertest2;




import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class LocationService extends Service{
	   private Notification notify1;
	   Timer _timer;
	    @SuppressLint("NewApi")
		public void onCreate() {

			super.onCreate();
			Log.v("LocationService", "OnCreate");
			
			RegisterLocationListener();
			
			   Toast.makeText(getApplicationContext(), "LocationService Created。。。",
		                Toast.LENGTH_SHORT).show();
				_timer = new Timer();
				_timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Log.v("wang", "服务还活着...");
						DoEvent();
						//_Ling_handler.postDelayed(_Ling_runnable, 1000);
					}
				}, 3000, 60000);// 一开始延迟3秒执行TimerTask,接着延迟60秒执行TimerTask,接着延迟60秒执行TimerTask
				
				
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                new Handler(getMainLooper()).post(new Runnable() {
	                    @Override
	                    public void run() {
	                        
	                    }						
	                });
	            }
	        }).start();
	        //设置图片,通知标题,发送时间,提示方式等属性
//            Intent it = new Intent(LocationService.this, Map4Activity.class);
//            PendingIntent pit = PendingIntent.getActivity(LocationService.this, 0, it, 0);
            Notification.Builder mBuilder = new Notification.Builder(LocationService.this.getApplicationContext());
            mBuilder.setContentTitle("LocationService Start")                        //标题
                    .setContentText("~")      //内容
                    .setSubText("SubText")                    //内容下面的一小段文字
                    .setTicker("Ticker")             //收到信息后状态栏显示的文字信息
                    .setWhen(System.currentTimeMillis())           //设置通知时间
//                    .setSmallIcon(R.mipmap.ic_lol_icon)            //设置小图标
//                    .setLargeIcon(LargeBitmap)                     //设置大图标
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
//                    .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.biaobiao))  //设置自定义的提示音
                    .setAutoCancel(true);                           //设置点击后取消Notification
//                    .setContentIntent(pit);                        //设置PendingIntent
            notify1 = mBuilder.build();
            //mNManager.notify(NOTIFYID_1, notify1);
        	startForeground(1, notify1);
	    }
	    private void RegisterLocationListener() {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(Map4Activity.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            else 
            {
            	List<String> lp = lm.getAllProviders();
        		for (String item : lp) {
        			boolean isProviderEnabled = lm.isProviderEnabled(item);
        			Log.i("8023", "位置服务：" + item + " 状态:" + isProviderEnabled);
        			// 位置服务
        			//if (isProviderEnabled)
        			{
        				long minTime=10000;
        				float minDistance=20;
        				String locationProvider = LocationManager.NETWORK_PROVIDER;
        				if (item .equals( locationProvider)) {
        					lm.requestLocationUpdates(locationProvider, minTime, minDistance,
        							new NetWork_locationListener());
        				} else if (item.equals(LocationManager.GPS_PROVIDER)  ) {
        					lm.requestLocationUpdates("gps", minTime, minDistance,
        							new GPS_locationListener());
        				}
        			}
        		}
                
            }
		}
	    private void DoEvent() {
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
			List<String> lp = lm.getAllProviders();
			for (String item : lp) {
				boolean isProviderEnabled = lm.isProviderEnabled(item);
				Log.i("8023", "位置服务：" + item + " 状态:" + isProviderEnabled);
				// 位置服务
				//if (isProviderEnabled)
			/*	{
					String locationProvider = LocationManager.NETWORK_PROVIDER;
					if (item .equals( locationProvider)) {
						lm.requestLocationUpdates(locationProvider, 1000, 5,
								new NetWork_locationListener());
					} else if (item.equals(LocationManager.GPS_PROVIDER)  ) {
						lm.requestLocationUpdates("gps", 1000, 5,
								new GPS_locationListener());
					}
				}*/

				Criteria criteria = new Criteria();
				criteria.setCostAllowed(false);
				// 设置位置服务免费
				criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 设置水平位置精度
				// getBestProvider 只有允许访问调用活动的位置供应商将被返回
				// String providerName = lm.getBestProvider(criteria, true);
				String providerName = lm.getProvider(item).getName();
				Log.i("8023", "------位置服务：" + providerName);

				if (providerName != null) {
					Location location = lm.getLastKnownLocation(providerName);
					if (location != null) {
						Log.i("8023", "-------" + location);
						// 获取维度信息
						double latitude = location.getLatitude();
						// 获取经度信息
						double longitude = location.getLongitude();
						String result = providerName + "定位方式： " + providerName
								+ "  维度：" + latitude + "  经度：" + longitude;
						
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
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
}
