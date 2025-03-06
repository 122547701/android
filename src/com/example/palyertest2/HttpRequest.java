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
			// 打开和URL之间的连接
			 conn = (HttpURLConnection)realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			//conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setConnectTimeout(30000);
			// 发送POST请求必须设置如下两行
			if (param != null && param != "") {
				conn.setDoOutput(true);
				conn.setDoInput(true);
				// 获取URLConnection对象对应的输出流
				_OutputStream=conn.getOutputStream();
				out = new PrintWriter(_OutputStream);
				// 发送请求参数
				out.print(param);
				// flush输出流的缓冲
				out.flush();
				out.close();
				_OutputStream.close();				
			}
			_InputStream=conn.getInputStream();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(_InputStream));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			if(result==null||result=="")
			{
				throw new Exception("无结果!");
			}
			
			 in.close();
			 _InputStream.close();
			 conn.disconnect();
			 conn=null;
			 realUrl=null;
			 RequestSuccessed[0]=true;
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
			java.util.Date Date = new java.util.Date();
			FileService.write(Date.toString() +":"+e.toString(),"Error.txt",false);
			return  e.toString();
		}
		// 使用finally块来关闭输出流、输入流
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
