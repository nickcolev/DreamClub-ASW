package com.vera5.stopwatch;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebAppInterface {

  private final static String TAG = "ASW";
  private final MainActivity mParent;
  private final String log;

	WebAppInterface(MainActivity ma) {
        mParent = ma;
        //log = Environment.getExternalStorageDirectory().getAbsolutePath()+"/stopwatch.tsv";
		log = "/data/data/com.vera5.stopwatch/files/stopwatch.tsv";
	}

	private void Tooltip(String s) {
		Toast.makeText(mParent, s, Toast.LENGTH_SHORT).show();
	}

	public static String now() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public void save(String time, String tag) {
		// CSV: datetime time tag
		// TODO Add GPS
		// http://developer.android.com/reference/android/location/LocationManager.html
		// LocationManager.getLastKnownLocation()
		String row = now()+"\t"+time+"\t"+tag+"\n";
		try {
			FileOutputStream out = new FileOutputStream(MainActivity.logName(),true);
			out.write(row.getBytes());
			out.close();
			Tooltip("Saved");
		} catch (IOException e) {
			Log.e(mParent.TAG, e.getMessage());
		}
	}
}
