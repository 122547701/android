package com.example.palyertest2;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class NetWork_locationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
    	 //��ȡγ��
        Double latitude = location.getLatitude();
        //��ȡ����
        Double longitude = location.getLongitude();
        Log.e("Latitude from NetWork", String.valueOf(latitude));
        Log.e("Longitude from NetWork", String.valueOf(longitude));
        java.util.Date DateNow = new java.util.Date();
        String sentMSG= TransmitReceiver. CheckLocation(longitude,latitude,locationActivity._MaxDistance);
         FileService.write("\n\r"+DateNow.toString()+ " from NETWORK"+ sentMSG         		
         		,"NETWORK_locationListener.txt",  true);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("λ���ṩ����"+provider, "����");
    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
