package com.example.palyertest2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Intent _Intentservice;
		if (intent.getAction().equals("VIDEO_TIMER")) {
			_Intentservice = new Intent(context, TimerService.class); // ÒªÆô¶¯µÄActivity
			_Intentservice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(_Intentservice);

		}
	}
}
