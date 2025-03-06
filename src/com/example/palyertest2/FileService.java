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
                Environment.MEDIA_MOUNTED)) { // 如果sdcard存在
        	String Path=Environment.getExternalStorageDirectory()
                    .toString()
                    + File.separator
                    + DIR_root
                    + File.separator;
            File file = new File(Path+
                   ((SubDir==null||SubDir.equals(""))?"": (SubDir+ File.separator))
                    + FILENAME); // 定义File类对象
            File parentFile=file.getParentFile();
            if (!parentFile.exists()) { // 父文件夹不存在
            	parentFile.mkdirs(); // 创建文件夹
            }
//           if( file.exists())
//           {
//        	   file.delete();
//           }
            PrintStream out = null; // 打印流对象用于输出
            try {
                out = new PrintStream(new FileOutputStream(file,append)); // 追加文件
                out.println(content);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                LogErr(content, Path);
            } finally {
                if (out != null) {
                    out.close(); // 关闭打印流
                }
            }
        } else { // SDCard不存在，使用Toast提示用户
          
        }
        return false;
    }
private static void LogErr(String content, String Path) {
	File fileErro = new File(Path
	       
	         + Error); 
	PrintStream outError = null; // 打印流对象用于输出
	try {
		outError = new PrintStream(new FileOutputStream(fileErro,true)); // 追加文件
		outError.println(content);
	}catch(Exception ee)
	{
		 ee.printStackTrace();
	}finally {
	    if (outError != null) {
	    	outError.close(); // 关闭打印流
	    }
	}
}

    // 文件读操作函数
    public static String read(String Path) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 如果sdcard存在
            File file = new File(Path); // 定义File类对象
            if (!file.getParentFile().exists()) { // 父文件夹不存在
                file.getParentFile().mkdirs(); // 创建文件夹
            }
            Scanner scan = null; // 扫描输入
            StringBuilder sb = new StringBuilder();
            try {
                scan = new Scanner(new FileInputStream(file)); // 实例化Scanner
                while (scan.hasNext()) { // 循环读取
                    sb.append(scan.next() + "\n"); // 设置文本
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (scan != null) {
                    scan.close(); // 关闭打印流
                }
            }
        } else { // SDCard不存在，使用Toast提示用户
           // Toast.makeText(this, "读取失败，SD卡不存在！", Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
