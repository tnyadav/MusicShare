package com.musicsharing.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class JainButton extends Button {

	public JainButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public JainButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public JainButton(Context context) {
		super(context);
		init();
	}

	private void init() {
		/*Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
		"fonts/calibri.otf");*/
Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
"fonts/Foglihten.otf");

		setPadding(10, 10, 10, 10);
	}
}