package com.vera5.stopwatch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Logger extends Activity {

  private static TextView mLog;
  private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = MainActivity.context;
		setContentView(R.layout.log);
		mLog = (TextView) findViewById(R.id.log);
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			BufferedReader in = new BufferedReader(
				new FileReader(MainActivity.logName()));
			String row;
			String[] a;
			while ((row = in.readLine()) != null) {
				a = row.split("\t");
				mLog.append("\n"+a[1]+"\t"+a[2]);
			}
			in.close();
		} catch (IOException e) {
			Log.e("ASW01Logger", e.getMessage());
		}
	}

}
