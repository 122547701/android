package com.example.palyertest2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import android.os.Environment;
import android.widget.Toast;

public class FileService {
 static	String DIR_root="Weather",FILENAME="HtmlSource.txt",Gd_Weather_FileName="GD_Weather_HtmlSource.txt",
		 SendSMSResult="SendSMSResult.txt",ReceivedSMS="ReceiveSMS.txt",Error="Error.txt";
 public static boolean write(String content,String FILENAME,boolean append)
 {
	 return write(content,FILENAME,null,append);
 }
 public static boolean write(String content,String FILENAME,String SubDir, boolean append) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // ���sdcard����
        	String Path=Environment.getExternalStorageDirectory()
                    .toString()
                    + File.separator
                    + DIR_root
                    + File.separator;
            File file = new File(Path+
                   ((SubDir==null||SubDir.equals(""))?"": (SubDir+ File.separator))
                    + FILENAME); // ����File�����
            File parentFile=file.getParentFile();
            if (!parentFile.exists()) { // ���ļ��в�����
            	parentFile.mkdirs(); // �����ļ���
            }
//           if( file.exists())
//           {
//        	   file.delete();
//           }
            PrintStream out = null; // ��ӡ�������������
            try {
                out = new PrintStream(new FileOutputStream(file,append)); // ׷���ļ�
                out.println(content);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                LogErr(content, Path);
            } finally {
                if (out != null) {
                    out.close(); // �رմ�ӡ��
                }
            }
        } else { // SDCard�����ڣ�ʹ��Toast��ʾ�û�
          
        }
        return false;
    }
private static void LogErr(String content, String Path) {
	File fileErro = new File(Path
	       
	         + Error); 
	PrintStream outError = null; // ��ӡ�������������
	try {
		outError = new PrintStream(new FileOutputStream(fileErro,true)); // ׷���ļ�
		outError.println(content);
	}catch(Exception ee)
	{
		 ee.printStackTrace();
	}finally {
	    if (outError != null) {
	    	outError.close(); // �رմ�ӡ��
	    }
	}
}

    // �ļ�����������
    public static String read(String Path) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // ���sdcard����
            File file = new File(Path); // ����File�����
            if (!file.getParentFile().exists()) { // ���ļ��в�����
                file.getParentFile().mkdirs(); // �����ļ���
            }
            Scanner scan = null; // ɨ������
            StringBuilder sb = new StringBuilder();
            try {
                scan = new Scanner(new FileInputStream(file)); // ʵ����Scanner
                while (scan.hasNext()) { // ѭ����ȡ
                    sb.append(scan.next() + "\n"); // �����ı�
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (scan != null) {
                    scan.close(); // �رմ�ӡ��
                }
            }
        } else { // SDCard�����ڣ�ʹ��Toast��ʾ�û�
           // Toast.makeText(this, "��ȡʧ�ܣ�SD�������ڣ�", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
