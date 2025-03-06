package com.example.palyertest2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PlayerTest2MainActivity extends Activity {
	public static String time0Str ="Timer0"; 
	public static String time1Str="Timer1";
	protected static final int GET_CODE = 12120;
	static private int openfileDialogId = 0;
	public static String _PreferenceName = "VIDEO";
	Intent _intent = null,_Intentservice=null,_location_Intentservice;
	
	
	
	
	PendingIntent _pendingIntent = null;
	AlarmManager _am = null;
	
	// static Timer timer = new Timer(false);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_test2_main);

	
	
		// ���õ�����ťʱ���ļ��Ի���
		findViewById(R.id.btnSelectFile).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showDialog(openfileDialogId);
					}
				});
		View vbtnLocation=findViewById(R.id.btnLocation);
		vbtnLocation.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(PlayerTest2MainActivity.this, locationActivity.class);
						
						
						startActivity(intent);
						//startActivityForResult(intent,GET_CODE);
					}
				});
	
		
		Context ctx = PlayerTest2MainActivity.this;

		SharedPreferences settings = ctx.getSharedPreferences(_PreferenceName,
			MODE_PRIVATE);
		// ��������
		 //SharedPreferences settings=getPreferences(Activity.MODE_PRIVATE);
//		int SetHour = settings.getInt("Hour", 8);
//		int SetMin = settings.getInt("Minute", 5);
		 
		 Global.inDebug= settings.getInt(String.valueOf( R.id.chkDebug), 0);
		 
		String _FilePath = settings.getString("FilePath", "");
		TextView txtFilePath = (TextView) findViewById(R.id.txtFilePath);
		txtFilePath.setText(_FilePath);
		
		String path= Environment.getExternalStorageDirectory().toString() + File.separator+ FileService.DIR_root+ File.separator;
		String Result=FileService.read(path+ FileService.FILENAME);
		if(Result!=null)
		{
			TextView txtResult = (TextView) findViewById(R.id.txtResult);
		txtResult.setText(Result);
		txtResult.setMovementMethod(ScrollingMovementMethod.getInstance());
		}
		 Result=FileService.read(path+ FileService.Gd_Weather_FileName);
		if(Result!=null)
		{
			TextView txtResult2 = (TextView) findViewById(R.id.txtResult2);
			txtResult2.setText(Result);
			txtResult2.setMovementMethod(ScrollingMovementMethod.getInstance());
		}
		
		TextView txtLastCheckTime = (TextView) findViewById(R.id.txtLastCheckTime);
		txtLastCheckTime.setText(settings.getString("LastCheckTime", ""));
		/*
		 * if(timer!=null) {timer.cancel();} timer = new Timer(false);
		 * timer.schedule(task, 60000, 60000); // 1s��ִ��task,����1s�ٴ�ִ��
		 * //timer.scheduleAtFixedRate(task, when, period)
		 */
		BindListView();
        
		
		// Intent intent=new Intent("com.example.AutoReceiver");
		/*if(_intent==null)
		{
		_intent = new Intent(this, AutoReceiver.class);
		_intent.setAction("VIDEO_TIMER");
		_pendingIntent = PendingIntent.getBroadcast(this, 0, _intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		_am = (AlarmManager) getSystemService(ALARM_SERVICE);
		_am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				60 * 1000, _pendingIntent);
		}
		*/
		if(_Intentservice==null)
		{
			_Intentservice = new Intent(this, TimerService.class); // Ҫ������Activity
			_Intentservice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startService(_Intentservice);
		}
		findViewById(R.id.btnStop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				TimerService.Stop();
			}
		});
//		if(_location_Intentservice==null)
//		{
//			_location_Intentservice = new Intent(this, LocationService.class); // Ҫ������Activity
//			_location_Intentservice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			this.startService(_location_Intentservice);
//		}
	}

	private void BindListView() {
		ListView _listView =  (ListView)findViewById(R.id.lvDateTime);
		/*����һ����̬����*/          
		 
		ArrayList<HashMap<String, Object>> listItem=getData();
		        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//��Ҫ�󶨵�����                
		R.layout.list_item,//ÿһ�еĲ���//��̬�����е�����Դ�ļ���Ӧ�����岼�ֵ�View��
		new String[] {"ItemName", "ItemValue"},   
		new int[] {R.id.ItemText,R.id.ItemValue});

		        _listView.setAdapter(mSimpleAdapter);//ΪListView�������� 
       // _listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,));
_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO �Զ����ɵķ������
		Intent intent = new Intent();
		TextView c = (TextView) arg1.findViewById(R.id.ItemValue);
	    String playerChanged = c.getText().toString();
		intent.putExtra("SelectedValue", playerChanged);
		intent.setClass(PlayerTest2MainActivity.this, ParaSetActivity.class);
		//startActivity(intent);
		startActivityForResult(intent,GET_CODE);
	}
});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_test2_main, menu);
		return true;
	}
	private ArrayList<HashMap<String, Object>> getData(){
      
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();/*�������д������*/
		        
        SharedPreferences settings = getSharedPreferences(_PreferenceName,
				MODE_PRIVATE);
		// ��������
		 //SharedPreferences settings=getPreferences(Activity.MODE_PRIVATE);
		
		String Timer1Value=settings.getString(time1Str, "17:58");
		String Timer0Value=settings.getString(time0Str, "8:5");
		
	    HashMap<String, Object> map = new HashMap<String, Object>();  
				
        map.put("ItemName", Timer0Value);  
        map.put("ItemValue",time0Str+"#"+ Timer0Value);  
        listItem.add(map);  
        map = new HashMap<String, Object>();  
		
        map.put("ItemName", Timer1Value);  
        map.put("ItemValue",time1Str+"#"+ Timer1Value);  
        listItem.add(map);  
			  
        return listItem;
	}
	/*
	 * TimerTask task = new TimerTask() {
	 * 
	 * @Override public void run() { // ��Ҫ������:������Ϣ Message message = new
	 * Message(); message.what = 1; handler.sendMessage(message); } }; Handler
	 * handler = new Handler() { public void handleMessage(Message msg) { if
	 * (msg.what == 1) { //tvShow.setText(Integer.toString(i++));
	 * /*java.util.Date DateNow=new java.util.Date() ; int
	 * CurrentMin=DateNow.getMinutes(); int CurrentHour=DateNow.getHours();
	 * if(CurrentHour==_SetHour&&CurrentMin==_SetMin) { new
	 * Thread(runnable).start();
	 * 
	 * 
	 * //Play(); }else { Context ctx = PlayerTest2MainActivity.this;
	 * 
	 * SharedPreferences sp = ctx.getSharedPreferences(_PreferenceName,
	 * MODE_PRIVATE); //��������
	 * 
	 * 
	 * 
	 * java.util.Date LastUpdateTime=new
	 * java.util.Date(sp.getString("LastUpdateTime", "1900-1-1"));
	 * 
	 * if(DateNow.getMonth()>LastUpdateTime.getMonth()||
	 * DateNow.getDay()>LastUpdateTime.getDay()) { //new
	 * Thread(runnable).start(); } } } super.handleMessage(msg); }; };
	 */
	/*
	 * public String getWeather(String City) { String
	 * url="http://www.webxml.com.cn/WebServices/WeatherWebService.asmx"
	 * ;//�ṩ�ӿڵĵ�ַ String soapaction="http://WebXml.com.cn/"; //������������server�����
	 * String Result=""; Service service=new Service(); try{ Call
	 * call=(Call)service.createCall(); call.setTargetEndpointAddress(url);
	 * call.setOperationName(new QName(soapaction,"getWeatherbyCityName"));
	 * //����Ҫ�����ĸ����� call.addParameter(new QName(soapaction,"theCityName"),
	 * //����Ҫ���ݵĲ��� org.apache.axis.encoding.XMLType.XSD_STRING,
	 * javax.xml.rpc.ParameterMode.IN); call.setReturnType(new
	 * QName(soapaction,"getWeatherbyCityName"),Vector.class); //Ҫ���ص��������ͣ��Զ������ͣ�
	 * 
	 * //
	 * call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//����׼�����ͣ�
	 * 
	 * call.setUseSOAPAction(true); call.setSOAPActionURI(soapaction +
	 * "getWeatherbyCityName");
	 * 
	 * Vector v=(Vector)call.invoke(new Object[]{City});//���÷��������ݲ���
	 * 
	 * for(int i=0;i<v.size();i++) { System.out.println(v.get(i));
	 * Result+=v.get(i); } return Result; }catch(Exception ex) {
	 * ex.printStackTrace(); Result=ex.toString(); } return Result; }
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == openfileDialogId) {
			Map<String, Integer> maps = new HashMap<String, Integer>();
			// ���漸�����ø��ļ����͵�ͼ�꣬ ��Ҫ���Ȱ�ͼ����ӵ���Դ�ļ���
			String sRoot=OpenFileDialog.getRoot();
			maps.put(sRoot, R.drawable.filedialog_root); // ��Ŀ¼ͼ��
			maps.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up); // ������һ���ͼ��
			maps.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder); // �ļ���ͼ��
			maps.put("mp3", R.drawable.filedialog_wavfile); // wav�ļ�ͼ��
			maps.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);
			Dialog dialog = OpenFileDialog.createDialog(id, this, "���ļ�",
					new CallbackBundle() {
						@Override
						public void callback(Bundle bundle) {
							String filepath = bundle.getString("path");

							Context ctx = PlayerTest2MainActivity.this;

							SharedPreferences uiState = ctx
									.getSharedPreferences(_PreferenceName,
											MODE_PRIVATE);
							// ��������
							// SharedPreferences uiState=getPreferences(0);
							SharedPreferences.Editor editor = uiState.edit();
							editor.putString("FilePath", filepath);
							editor.commit();
							TextView txtFilePath = (TextView) findViewById(R.id.txtFilePath);
							txtFilePath.setText(filepath);
							setTitle(filepath); // ���ļ�·����ʾ�ڱ�����
						}
					}, ".mp3;", maps);
			return dialog;
		}
		return null;
	}
		@Override   
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	   {   
	    if(requestCode == GET_CODE)
	    {   
	     if(resultCode == RESULT_CANCELED)	 
	      {   
	        //text2.setText("����˷���");   
	      } 
	      else 
	      {   
	        if (data != null)
	         {   
	        	BindListView();
	          // text2.setText("�õ��ڶ���activity���صĽ��:\n"+data.getAction());   
	          }   
	      }   
	    }   
	   }  
	
}
