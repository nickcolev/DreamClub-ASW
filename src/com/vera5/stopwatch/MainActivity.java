package com.vera5.stopwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;
//import java.io.File;

public class MainActivity extends Activity {

  protected static final String TAG = "ASW01";
  protected static Context context;
  private static final String logfile = "stopwatch.tsv";
  private WebView myWebView;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getApplicationContext();
		setContentView(R.layout.main);
		myWebView = (WebView) findViewById(R.id.webview);
		// Enable JavaScript
		myWebView.getSettings().setJavaScriptEnabled(true);
		// Enable Alert
		myWebView.setWebChromeClient(new WebChromeClient());
		// Bind JS and Android code
		myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
		// Load a HTML from assets
		myWebView.loadUrl("file:///android_asset/stopwatch.htm");
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				//Toast.makeText(this, "Vol. -", Toast.LENGTH_SHORT).show();
				myWebView.loadUrl("javascript:ctrl()");
				return false; 
			case KeyEvent.KEYCODE_POWER:
				//Toast.makeText(this, "Power", Toast.LENGTH_SHORT).show();
				return true; 
		}
		return super.onKeyUp(keyCode,event);
	}

	protected static String logName() {
		return context.getFilesDir().getPath()+"/"+logfile;
	}

	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.log:
				try {
					startActivity(new Intent(".Logger"));
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				return true;
			case R.id.about:
				try {
					startActivity(new Intent(".About"));
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
