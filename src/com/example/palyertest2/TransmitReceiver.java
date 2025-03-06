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
		// �������Ź㲥
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");// ��ȡ��������
			for (Object pdu : pdus) {
				byte[] data = (byte[]) pdu;
				SmsMessage message = SmsMessage.createFromPdu(data);// ʹ��pdu��ʽ�Ķ����������ɶ��Ŷ���
				String sender = message.getOriginatingAddress();// ��ȡ���ŵķ�����
				String content = message.getMessageBody();// ��ȡ���ŵ�����
				Date date = new Date(message.getTimestampMillis());
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				String sendtime = format.format(date);
				/*
				 * SmsManager manager = SmsManager.getDefault();
				 * manager.sendTextMessage("5556", null, , null, null);//
				 * �����ص��Ķ��ŷ��͵���ָ�����ֻ����˴�Ϊ5556
				 */
				String contentMix = "������:" + sender + "-----����ʱ��:" + sendtime
						+ "----����:" + content;
				FileService.write("\n\r�յ�����;����:" + contentMix,
						FileService.ReceivedSMS, true);
				if (sender.contains("1112178") || sender.contains("1012178")) {

					/** �зֶ��ţ�ÿ��ʮ��������һ�������ų������Ʋ�����ʮ��ֻ��һ�������ص����ַ�����List���� */
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

	public static void transmitMessageTo(String phoneNumber, String message) {// ת������
		SmsManager manager = SmsManager.getDefault();
		/** �зֶ��ţ�ÿ��ʮ��������һ�������ų������Ʋ�����ʮ��ֻ��һ�������ص����ַ�����List���� */
		List<String> texts = manager.divideMessage(message);// ���������
		for (String text : texts) {
			manager.sendTextMessage(phoneNumber, null, text, null, null);
		}
		FileService.write("\n\r�ɹ����͵�" + phoneNumber + ";����:" + message,
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
				+"����"+longitude+"γ��"+Latitude+ "������"
				+ (f0) + "(m)";
		if (distance2 < MaxDistance) {
			
			SendMSG_DontRepeat(Global.phoneNumber,msg);
			
//			else {
//				transmitMessageTo(Global.phoneNumber, "�������"
//						+ (distance1 * Global.OneLongitudeEqualMetre) + "(��)");
//			}
		}
		return msg;
	}
}
