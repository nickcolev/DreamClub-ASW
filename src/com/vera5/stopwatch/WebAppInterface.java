package com.vera5.stopwatch;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebAppInterface {

  private final static String TAG = "ASW";
  private final MainActivity mParent;
  private final String log;

	WebAppInterface(MainActivity ma) {
        mParent = ma;
        log = Environment.getExternalStorageDirectory().getAbsolutePath()+"/stopwatch.tsv";
	}

	private void Tooltip(String s) {
		Toast.makeText(mParent, s, Toast.LENGTH_SHORT).show();
	}

	public static String now() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public void save(String tag, String time) {
		// CSV: datetime time tag
		// TODO Add GPS
		// http://developer.android.com/reference/android/location/LocationManager.html
		// LocationManager.getLastKnownLocation()
		String row = now()+"\t"+time+"\t"+tag+"\n";
		try {
			FileWriter fw = new FileWriter(log, true);
			fw.write(row);
			fw.close();
			Tooltip("Saved");
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
