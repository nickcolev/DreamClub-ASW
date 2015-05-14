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
		mLog.append(getLog());
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
					intent.putExtra(Intent.EXTRA_TEXT, getLog());
					if (intent.resolveActivity(getPackageManager()) != null)
						startActivity(intent);
    			} catch (Exception e) {
					Log.e(MainActivity.TAG, e.getMessage());
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private String getLog() {
		String na = "Nothing logged yet";
		File f = new File(MainActivity.logName());
		if (!f.exists()) return na;
		String log = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String row;
			String[] a;
			while ((row = in.readLine()) != null) {
				a = row.split("\t");
				log += "\n"+a[1]+"\t"+a[2];
			}
			in.close();
		} catch (IOException e) {
			log = e.getMessage();
			Log.e("ASW01Logger", e.getMessage());
		}
		return (log.length() > 5 ? "Time\t\tTag"+log : na);
	}
}
