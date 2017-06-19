package com.m520it.musicmidea.fileUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUtil {

	public static final int STOP_WHEN_OVER = 0;
	public static final int SINGLE_LOOP = 1;
	public static final int ALL_LOOP = 2;

	public static final String SHARD_NAME = "music_setting";
	public static final String SHARD_KEY = "mode";

	public static void saveData(Context c, int playMode) {
		SharedPreferences sp = c.getSharedPreferences(SHARD_NAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(SHARD_KEY, playMode);
		edit.commit();
	}
	
	public static int getPlayMode(Context c){
		SharedPreferences sp = c.getSharedPreferences(SHARD_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(SHARD_KEY, STOP_WHEN_OVER);
	}

}
