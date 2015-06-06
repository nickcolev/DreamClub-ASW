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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MainActivity extends Activity {

  protected static final String TAG = "ASW01";
  protected static Context context;
  private WebView myWebView;
  private boolean isLongKeyPress = false;
  private boolean isMove = false;
  private float X0=0, Y0=0;

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
	public void onStart() {
		super.onStart();
		// Long touch
		myWebView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				switch(e.getAction()) {
					case 0:		// Down
						X0 = e.getX();
						Y0 = e.getY();
						break;
					case 1:		// Up
						if (isMove) {
							isMove = false;
							myWebView.loadUrl("javascript:setLastColor()");
							if (isSwipe(e)) myWebView.loadUrl("javascript:reset(false)");
						}
						break;
					case 2:		// Move (swipe)
						isMove = true;
						break;
				}
				return false;
			}
		});
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
				if(isLongKeyPress)		// There is isLongPress(), but doesn't work on MB511
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

	// Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	private boolean isSwipe(MotionEvent e) {
		int x1 = Math.round(X0),
			y1 = Math.round(Y0),
			x2 = Math.round(e.getX()),
			y2 = Math.round(e.getY()),
			dx = Math.abs(x1-x2),
			dy = Math.abs(y1-y2);
			if (dy == 0) dy = 1;	// Avoid division by zero
			// Adjust if necessary
			return (dx > 50 && ((dx / dy) > 5) ? true : false);
	}

}
