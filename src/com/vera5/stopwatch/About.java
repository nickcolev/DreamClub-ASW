package com.vera5.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class About extends Activity {

  private WebView myWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		myWebView = (WebView) findViewById(R.id.about);
		myWebView.loadUrl("file:///android_asset/about.htm");
	}

}
