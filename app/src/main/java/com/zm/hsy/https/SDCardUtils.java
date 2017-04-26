package com.zm.hsy.https;

import java.io.File;

import android.os.Environment;

public class SDCardUtils {
	public static final String DISK_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator+"Liulianginn";
	public static final String IMAGE_DIR = DISK_DIR + File.separator+"imagecache";
	public static int loadFailedImageId = 0;//R.drawable.**
	public static int loadingImageId = 0;//R.drawable.**
	static{
		File dir = new File(DISK_DIR);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File dir2 = new File(IMAGE_DIR);
		if(!dir2.exists()){
			dir2.mkdirs();
		}
	}
}
