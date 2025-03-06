package com.example.palyertest2;

import java.util.Calendar;
import java.util.Date;

import android.graphics.PointF;

public class CommonService {
	public static int getWeekOfDate(Date dt) {
		// String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
		// "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}

//	public static double GetDistance(double x, double y, PointF point_ZSJNT) {
//		// double x_dx=113.269592, y_dx=23.134377;
//		//计算程序需要转化为弧度（*3.1415926/180）地球半径为R=6371.0 km，则两点距离d=R*arcos[cos(Y1)*cos(Y2)*cos(X1-X2)+sin(Y1)*sin(Y2)]。
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
	private static final double EARTH_RADIUS = 6371000; // 平均半径,单位：m；不是赤道半径。赤道为6378左右
	public static double GetDistance(Double lat1,Double lng1,Double lat2,Double lng2) {
	        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
	        double radiansAX = Math.toRadians(lng1); // A经弧度
	        double radiansAY = Math.toRadians(lat1); // A纬弧度
	        double radiansBX = Math.toRadians(lng2); // B经弧度
	        double radiansBY = Math.toRadians(lat2); // B纬弧度

	        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
	        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
	                + Math.sin(radiansAY) * Math.sin(radiansBY);
//	        System.out.println("cos = " + cos); // 值域[-1,1]
	        double acos = Math.acos(cos); // 反余弦值
//	        System.out.println("acos = " + acos); // 值域[0,π]
//	        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
	        return EARTH_RADIUS * acos; // 最终结果
	    }
}
