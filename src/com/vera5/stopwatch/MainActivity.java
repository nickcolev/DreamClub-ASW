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
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import android.view.MotionEvent;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {

  protected static final String TAG = "ASW01";
  protected static Context context;
  private WebView myWebView;
  private boolean isLongKeyPress = false;
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
//int action = e.getAction();
//Log.d("***MA***", "action: "+action+", Time "+e.getDownTime()+"ms, Pressure: "+e.getPressure()+", Size: "+e.getSize()+", x/y: "+Math.round(e.getX())+"/"+Math.round(e.getY()));
				switch(e.getAction()) {
					case 0:		// Down
						X0 = e.getX();
						Y0 = e.getY();
						break;
					case 1:		// Up
						if (isLong(e)) {
							//dbg(e);
							myWebView.loadUrl("javascript:reset()");
							return true;
						}
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

	private void dbg(MotionEvent e) {
		Tooltip("Time: "+(e.getEventTime()-e.getDownTime())+"ms"
			+", Size: "+(e.getSize()*100)
			+", Pressure: "+e.getPressure()
			+", x/y: "+Math.round(e.getX())+"/"+Math.round(e.getY())
			//+", was: "+Math.round(e.getHistoricalX(0))+"/"+Math.round(e.getHistoricalY(0))
			+", p: "+Math.round(X0)+"/"+Math.round(Y0)
		);
	}

	private boolean isLong(MotionEvent e) {
		if ((e.getEventTime()-e.getDownTime()) < 550 ||
			Math.abs(Math.round(e.getX())-X0) > 5 ||
			Math.abs(Math.round(e.getY())-Y0) > 5) return false;
		return true;
	}

	private void Tooltip(String s) {
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}

}
