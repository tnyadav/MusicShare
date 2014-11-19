package com.musicsharing.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class JainTextView extends TextView {

	public JainTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public JainTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public JainTextView(Context context) {
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