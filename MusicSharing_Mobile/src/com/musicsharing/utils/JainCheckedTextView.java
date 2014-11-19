package com.musicsharing.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class JainCheckedTextView extends TextView {

	public JainCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public JainCheckedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public JainCheckedTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		/*Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/calibri.otf");*/
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Foglihten.otf");
		
		setTypeface(tf);
		
	}
}