package com.example.forphp.utils;

import android.util.Log;

public class UtilDebug {
	public static final String TAG_MAINACTIVITY = "forphp.mainactivity";

	public static void di(String tag, String msg) {
		Log.i(tag, msg);	
	}
}
