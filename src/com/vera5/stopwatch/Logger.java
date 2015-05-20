package com.vera5.stopwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger extends Activity {

  private static final String logfile = "stopwatch.tsv";
  private static TextView mLog;
  private Context ctx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		mLog = (TextView) findViewById(R.id.log);
		this.ctx = getApplicationContext();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mLog.append(get(false));
	}

	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.send:
				try {
					// https://developer.android.com/guide/components/intents-common.html#Email
					// Attachment: setType("*/*") and EXTRA_STREAM
					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setData(Uri.parse("mailto:")); // only email apps should handle this
					intent.putExtra(Intent.EXTRA_SUBJECT, "StopWatch log");
					intent.putExtra(Intent.EXTRA_TEXT, get(true));
					if (intent.resolveActivity(getPackageManager()) != null)
						startActivity(intent);
    			} catch (Exception e) {
					Log.e(MainActivity.TAG, e.getMessage());
					Tooltip(e.getMessage());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/*
	* As of version 1, the log is a tab-separated CSV with format:
	*	datetime tag time split-times
	* where 'datetime' is measure time stamp
	*
	* TODO Add GPS
	*	http://developer.android.com/reference/android/location/LocationManager.html
	*	LocationManager.getLastKnownLocation()
	*/
	private String get(boolean extended) {
		String na = "Nothing logged yet";
		File f = new File(logName());
		if (!f.exists()) return na;
		String log = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String row;
			String[] a;
			while ((row = in.readLine()) != null) {
				a = row.split("\t");
				log += "\n" +
					(extended ?
						a[0]+"\t"+a[1]+"\t"+a[2]+(a.length > 3 ? "\t"+a[3] : "") :
						a[2]+"\t"+a[1]);
			}
			in.close();
		} catch (IOException e) {
			log = e.getMessage();
			Log.e(MainActivity.TAG, e.getMessage());
		}
		return (log.length() > 5 ? "Time\t\tTag"+log : na);
	}

	protected void put(String tag, String time, String split) {
		String  row = now()
			+"\t"+tag
			+"\t"+time
			+"\t"+getSplit(split)+"\n";
		try {
			FileOutputStream out = new FileOutputStream(logName(),true);
			out.write(row.getBytes());
			out.close();
			Tooltip("Saved");
		} catch (IOException e) {
			Log.e(MainActivity.TAG, e.getMessage());
		}
	}

	private String getSplit(String split) {
		return split.trim().replace("\n",",");
	}

	private void Tooltip(String s) {
		Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
	}

	private String logName() {
		return ctx.getFilesDir().getPath()+"/"+logfile;
	}

	public void setContext(Context ctx) {
		this.ctx = ctx;
	}

	protected static String now() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

}
