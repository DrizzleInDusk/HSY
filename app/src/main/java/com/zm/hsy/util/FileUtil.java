package com.zm.hsy.util;

import java.io.File;

import android.content.Context;

/**
 * 
 *     文件类，封装了path获取文件
 * 
 **/

public class FileUtil {
	private Context mContext;
	public FileUtil(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 从本地获取File
	 * 
	 * @param filePath
	 * @return File
	 */
	public static File getFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.setLastModified(System.currentTimeMillis());
			return file;
		}
		return null;
	}


}
