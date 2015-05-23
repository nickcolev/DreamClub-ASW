package com.vera5.stopwatch;

import android.content.Context;
import android.util.Log;

public class WebAppInterface {

  private final Context ctx;

	WebAppInterface(Context ctx) {
		this.ctx = ctx;
	}

	public void save(String tag, String time) {
		Logger log = new Logger();
		log.setContext(ctx);
		log.put(tag, time);
	}

}
