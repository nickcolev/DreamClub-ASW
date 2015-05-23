package com.vera5.stopwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {

  protected static final String TAG = "ASW01";
  protected static Context context;
  private WebView myWebView;
  private boolean isLongKeyPress = false;
 
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
			event.startTracking();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if(isLongKeyPress)	// There is isLongPress(), but doesn't work on MB511
					myWebView.loadUrl("javascript:reset()");
				else
					myWebView.loadUrl("javascript:ctrl()");
				isLongKeyPress = false;
				return true; 
		}
		return super.onKeyUp(keyCode,event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_VOLUME_UP:
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				isLongKeyPress = true;
				return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}

	protected void onSaveInstanceState(Bundle state) {
Log.d("***MA***", "onSaveInstanceState()");
		super.onSaveInstanceState(state);
	}

	@Override
	public void onDestroy() {
Log.d("***MA***", "onDestroy()");
		super.onDestroy();
	}

	protected void onPause() {
Log.d("***MA***", "onPause()");
		super.onPause();
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
			case R.id.about:
				try {
					startActivity(new Intent(".About"));
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				return true;
			case R.id.log:
				try {
					startActivity(new Intent(".Logger"));
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
				return true;
			case R.id.tag:
				myWebView.loadUrl("javascript:setTag()");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
