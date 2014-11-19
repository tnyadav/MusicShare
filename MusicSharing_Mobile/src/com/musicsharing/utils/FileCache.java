/*
 * File: FileCache.java
 * 
 * Cache for files
 */

package com.musicsharing.utils;

import java.io.File;

import android.content.Context;

/**
 * FileCache class denotes the cache of file
 */
public class FileCache {
	// Cache directory
	private File cacheDir;

	/**
	 * Initialize the cache
	 * 
	 * @param context
	 *            context of the activity
	 */
	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = context.getCacheDir();
		} else {
			cacheDir = context.getCacheDir();
		}

		// Create a cache if it does not exist
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	/**
	 * Get file from the URL
	 * 
	 * @param url
	 *            the URL of the image
	 * @return the FileObject for this image
	 */
	public File getFile(String url) {
		if (url == null) {
			return null;
		}

		String filename = String.valueOf(url.hashCode());
		File f = null;
		try {
			f = new File(cacheDir, filename);
		} catch (NullPointerException e) {
			return null;
		}
		return f;

	}

	/**
	 * Clears the cache
	 */
	public void clear() {
		File[] files = cacheDir.listFiles();

		if (files == null) {
			return;
		}

		for (File f : files) {
			f.delete();
		}
	}
}