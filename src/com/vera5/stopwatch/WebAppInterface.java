package com.vera5.stopwatch;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.widget.Toast;

public class WebAppInterface {

  private final MainActivity mParent;

	WebAppInterface(MainActivity ma) {
        mParent = ma;
	}

	private void Tooltip(String s) {
		Toast.makeText(mParent, s, Toast.LENGTH_SHORT).show();
	}


}
