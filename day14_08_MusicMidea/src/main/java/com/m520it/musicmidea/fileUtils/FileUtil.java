package com.m520it.musicmidea.fileUtils;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 520 on 2016/11/28.
 */

public class FileUtil {

    public static ArrayList<String> loadMusicFilePath(Context c) {
        ArrayList<String> result = new ArrayList<>();
        File filesDir = c.getFilesDir();
        for (File file : filesDir.listFiles()) {
            if (file.getAbsolutePath().endsWith(".mp3")) {
                result.add(file.getAbsolutePath());
            }

        }
        return result;
    }

    public static String getMusicFileName(String filePath){
        return filePath.substring(filePath.lastIndexOf("/")+1);
    }
}
