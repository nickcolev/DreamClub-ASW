package com.vera5.stopwatch;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class About extends Activity {

  private WebView myWebView;
  private final String url = "file:///android_asset/about.htm";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		myWebView = (WebView) findViewById(R.id.about);
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.setWebChromeClient(new WebChromeClient());
		myWebView.addJavascriptInterface(new JSInterface(this), "Android");
		myWebView.loadUrl(url);
	}

}


class JSInterface {

  private Context ctx;

	JSInterface(Context ctx) {
		this.ctx = ctx;
	}

	public String version() {
		try {
			PackageManager packageManager = ctx.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(ctx.getPackageName(),0);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(MainActivity.TAG, "Get app version failed with "+e.getMessage());
			return "?";
		}
	}
}
