package com.vera5.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import android.view.KeyEvent;

public class MainActivity extends Activity {

  private WebView myWebView;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		myWebView = (WebView) findViewById(R.id.webview);
		// Enable JavaScript
		myWebView.getSettings().setJavaScriptEnabled(true);
		// Enable Alert
		myWebView.setWebChromeClient(new WebChromeClient());
		// Enable "long-press" -- http://stackoverflow.com/questions/3449098/enable-longclick-in-webview
		myWebView.setLongClickable(true);
		// Bind JS and Android code
		myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
		// Load a HTML from assets
        myWebView.loadUrl("file:///android_asset/main.htm");
	}

	public boolean onKeyLongPress(int keyCode, KeyEvent event) {	// Doesn't work
Log.d("***", "onKeyLongPress()");
		return false;
	}

}
