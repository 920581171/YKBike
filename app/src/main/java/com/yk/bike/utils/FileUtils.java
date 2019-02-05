package com.yk.bike.utils;

import android.os.Environment;

import java.io.File;

public class FileUtils {
    public static String getDefaultCache(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/YKBikeCache");
        if (!file.exists())
            file.mkdirs();
        return file.getPath();
    }
}
