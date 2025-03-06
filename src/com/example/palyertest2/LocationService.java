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
			
			   Toast.makeText(getApplicationContext(), "LocationService Created������",
		                Toast.LENGTH_SHORT).show();
				_timer = new Timer();
				_timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Log.v("wang", "���񻹻���...");
						DoEvent();
						//_Ling_handler.postDelayed(_Ling_runnable, 1000);
					}
				}, 3000, 60000);// һ��ʼ�ӳ�3��ִ��TimerTask,�����ӳ�60��ִ��TimerTask,�����ӳ�60��ִ��TimerTask
				
				
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
	        //����ͼƬ,֪ͨ����,����ʱ��,��ʾ��ʽ������
//            Intent it = new Intent(LocationService.this, Map4Activity.class);
//            PendingIntent pit = PendingIntent.getActivity(LocationService.this, 0, it, 0);
            Notification.Builder mBuilder = new Notification.Builder(LocationService.this.getApplicationContext());
            mBuilder.setContentTitle("LocationService Start")                        //����
                    .setContentText("~")      //����
                    .setSubText("SubText")                    //���������һС������
                    .setTicker("Ticker")             //�յ���Ϣ��״̬����ʾ��������Ϣ
                    .setWhen(System.currentTimeMillis())           //����֪ͨʱ��
//                    .setSmallIcon(R.mipmap.ic_lol_icon)            //����Сͼ��
//                    .setLargeIcon(LargeBitmap)                     //���ô�ͼ��
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //����Ĭ�ϵ���ɫ��������
//                    .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.biaobiao))  //�����Զ������ʾ��
                    .setAutoCancel(true);                           //���õ����ȡ��Notification
//                    .setContentIntent(pit);                        //����PendingIntent
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
        			Log.i("8023", "λ�÷���" + item + " ״̬:" + isProviderEnabled);
        			// λ�÷���
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
			// ����������֪��λ���ṩ�ߵ������б�����δ��׼���ʻ���ûĿǰ��ͣ�õġ�
			List<String> lp = lm.getAllProviders();
			for (String item : lp) {
				boolean isProviderEnabled = lm.isProviderEnabled(item);
				Log.i("8023", "λ�÷���" + item + " ״̬:" + isProviderEnabled);
				// λ�÷���
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
				// ����λ�÷������
				criteria.setAccuracy(Criteria.ACCURACY_COARSE); // ����ˮƽλ�þ���
				// getBestProvider ֻ��������ʵ��û��λ�ù�Ӧ�̽�������
				// String providerName = lm.getBestProvider(criteria, true);
				String providerName = lm.getProvider(item).getName();
				Log.i("8023", "------λ�÷���" + providerName);

				if (providerName != null) {
					Location location = lm.getLastKnownLocation(providerName);
					if (location != null) {
						Log.i("8023", "-------" + location);
						// ��ȡά����Ϣ
						double latitude = location.getLatitude();
						// ��ȡ������Ϣ
						double longitude = location.getLongitude();
						String result = providerName + "��λ��ʽ�� " + providerName
								+ "  ά�ȣ�" + latitude + "  ���ȣ�" + longitude;
						
					} else {

						Toast.makeText(this, providerName + "������",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "1.������������ \n2.����ҵ�λ��",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO �Զ����ɵķ������
		return null;
	}
}
