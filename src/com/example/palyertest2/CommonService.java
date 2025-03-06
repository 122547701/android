package com.example.palyertest2;

import java.util.Calendar;
import java.util.Date;

import android.graphics.PointF;

public class CommonService {
	public static int getWeekOfDate(Date dt) {
		// String[] weekDays = {"������", "����һ", "���ڶ�", "������", "������", "������",
		// "������"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}

//	public static double GetDistance(double x, double y, PointF point_ZSJNT) {
//		// double x_dx=113.269592, y_dx=23.134377;
//		//���������Ҫת��Ϊ���ȣ�*3.1415926/180������뾶ΪR=6371.0 km�����������d=R*arcos[cos(Y1)*cos(Y2)*cos(X1-X2)+sin(Y1)*sin(Y2)]��
////		double distance1 = Math.sqrt(Math.pow((point_ZSJNT.x - x), 2)
////				+ Math.pow(point_ZSJNT.y - y, 2));
//		double distance1 =6371137*Math.acos(Math.cos(y)*Math.cos(point_ZSJNT.y)*Math.cos(x-point_ZSJNT.x)+Math.sin(y)*Math.sin(point_ZSJNT.y));
//		return distance1;
//	}
	/*
	public static double EarthRadiusKm = 6378.137; // WGS-84 
	public static double GetDistance(double p1Lat, double p1Lng,
	                          double p2Lat, double p2Lng)
	{

	    double dLat1InRad = p1Lat * (Math.PI / 180);
	    double dLong1InRad = p1Lng * (Math.PI / 180);
	    double dLat2InRad = p2Lat * (Math.PI / 180);
	    double dLong2InRad = p2Lng * (Math.PI / 180);
	    double dLongitude = dLong2InRad - dLong1InRad;
	    double dLatitude = dLat2InRad - dLat1InRad;
	    double a = Math.pow(Math.sin(dLatitude / 2), 2)
	                      + Math.cos(dLat1InRad) * Math.cos(dLat2InRad) 
	                      * Math.pow(Math.sin(dLongitude / 2), 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double dDistance = EarthRadiusKm * c;
	    return dDistance;
	}  */
	private static final double EARTH_RADIUS = 6371000; // ƽ���뾶,��λ��m�����ǳ���뾶�����Ϊ6378����
	public static double GetDistance(Double lat1,Double lng1,Double lat2,Double lng2) {
	        // ��γ�ȣ��Ƕȣ�ת���ȡ����������������Ե���Math.cos��Math.sin
	        double radiansAX = Math.toRadians(lng1); // A������
	        double radiansAY = Math.toRadians(lat1); // Aγ����
	        double radiansBX = Math.toRadians(lng2); // B������
	        double radiansBY = Math.toRadians(lat2); // Bγ����

	        // ��ʽ�С�cos��1cos��2cos����1-��2��+sin��1sin��2���Ĳ��֣��õ���AOB��cosֵ
	        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
	                + Math.sin(radiansAY) * Math.sin(radiansBY);
//	        System.out.println("cos = " + cos); // ֵ��[-1,1]
	        double acos = Math.acos(cos); // ������ֵ
//	        System.out.println("acos = " + acos); // ֵ��[0,��]
//	        System.out.println("��AOB = " + Math.toDegrees(acos)); // ���Ľ� ֵ��[0,180]
	        return EARTH_RADIUS * acos; // ���ս��
	    }
}
