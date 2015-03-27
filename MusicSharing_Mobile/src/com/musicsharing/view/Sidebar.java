package com.musicsharing.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.w3axis.sharehub.R;
import com.musicsharing.connections.ConnectionFragment;

/**
 * Class : Sidebar.java extends : View class Responsible for the side bar in the
 * existing store page
 * **/
public class Sidebar extends View {
	private char[] l;
	private SectionIndexer mSectionIndexter = null;
	private ListView mList;
	private TextView indexScroller;

	// Defining the constructors
	public Sidebar(Context context) {
		super(context);
		init();
	}

	public Sidebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Sidebar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// Function to initialise the side bar elements.
	private void init() {
		l = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z' };
		setBackgroundColor(0x44FFFFFF);
	}

	// Function to set the listview
	public void setListView(ListView _list, int width,TextView indexScroller) {
		mList = _list;
		mSectionIndexter = (SectionIndexer) _list.getAdapter();
		this.indexScroller=indexScroller;
	}

	// Defining the onTouch event on the sidebar
	public boolean onTouchEvent(MotionEvent event) {

		super.onTouchEvent(event);
		int i = (int) event.getY();
		int h = getMeasuredHeight() / 26;
		int idx = i / h;
		if (idx >= l.length) {
			idx = l.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {

			if (mSectionIndexter == null) {

				try {
					mSectionIndexter = (SectionIndexer) mList.getAdapter();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			// Getting the position of the element touched
			int position = mSectionIndexter.getPositionForSection(l[idx]);

			if (position == -1) {
				return true;
			}
			// setting the position of the list to the touched alphabet.
			mList.setSelection(position);

			indexScroller.setVisibility(View.VISIBLE);

			indexScroller.setText("" + l[idx]);

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					indexScroller.setVisibility(View.GONE);
				}
			}, 1000);

		}
		return true;
	}

	@SuppressLint("DrawAllocation")
	@SuppressWarnings("deprecation")
	// Defining the style for the sidebar
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(0xFFFFFFFF);
		paint.setTextSize(20);

		paint.setTextAlign(Paint.Align.CENTER);
		float widthCenter = getMeasuredWidth() / 2;
		float heightCenter = getMeasuredHeight() / 26;
		for (int i = 0; i < l.length; i++) {
			canvas.drawText(String.valueOf(l[i]), widthCenter, heightCenter
					+ (i * heightCenter), paint);

		}
		// if (android.os.Build.VERSION.SDK_INT >= 16) {
		// this.setBackground(getContext().getResources().getDrawable(
		// R.drawable.whiterectangle));
		// } else {
		this.setBackgroundDrawable(getContext().getResources().getDrawable(
				R.drawable.whiterectangle));
		// }

		super.onDraw(canvas);
	}
}
