package com.musicsharing.web;

import android.os.Bundle;

/**
 * This class serves as a listener for UI thread in all asynchronous tasks.
 * 
 * @author Atul Mittal
 * 
 */
public abstract class TAListener {

	/**
	 * To be called by the worker task indicating task completion.
	 * 
	 * @param argBundle
	 */
	public abstract void onTaskCompleted(String result);

	/**
	 * To be called by the worker task indicating task failure.
	 * 
	 * @param argBundle
	 */
	public abstract void onTaskFailed(String errorMessage);

}
