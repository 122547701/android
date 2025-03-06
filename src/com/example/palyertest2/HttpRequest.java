package com.example.palyertest2;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequest {
	public static String sendPost(String url, String param,Boolean[] RequestSuccessed) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		InputStream _InputStream=null;
		OutputStream _OutputStream=null;
		URL realUrl;
		HttpURLConnection conn=null;
		try {
			RequestSuccessed[0]=false;
			 realUrl = new URL(url);
			// �򿪺�URL֮�������
			 conn = (HttpURLConnection)realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			//conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(30000);
			// ����POST�������������������
			if (param != null && param != "") {
				conn.setDoOutput(true);
				conn.setDoInput(true);
				// ��ȡURLConnection�����Ӧ�������
				_OutputStream=conn.getOutputStream();
				out = new PrintWriter(_OutputStream);
				// �����������
				out.print(param);
				// flush������Ļ���
				out.flush();
				out.close();
				_OutputStream.close();				
			}
			_InputStream=conn.getInputStream();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(_InputStream));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			if(result==null||result=="")
			{
				throw new Exception("�޽��!");
			}
			
			 in.close();
			 _InputStream.close();
			 conn.disconnect();
			 conn=null;
			 realUrl=null;
			 RequestSuccessed[0]=true;
		} catch (Exception e) {
			System.out.println("���� POST ��������쳣��" + e);
			e.printStackTrace();
			java.util.Date Date = new java.util.Date();
			FileService.write(Date.toString() +":"+e.toString(),"Error.txt",false);
			return  e.toString();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if(_OutputStream!=null)
				{
					_OutputStream.close();
				}
				if (in != null) {
					in.close();
				}
				if(_InputStream!=null)
				{
					_InputStream.close();
				}
				if(conn!=null)
				{
				conn.disconnect();
				}
				conn=null;
				 realUrl=null;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}
