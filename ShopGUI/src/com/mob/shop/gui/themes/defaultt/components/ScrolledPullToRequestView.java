package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mob.tools.gui.PullToRequestAdatper;
import com.mob.tools.gui.PullToRequestView;

public class ScrolledPullToRequestView extends PullToRequestView {
	private PullToRequestAdatper adapter;
	int scrollTop = 0;
	int scrollPos = 0;

	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		try {
			ListView listView = ((ListView) ((ViewGroup) adapter.getBodyView()).getChildAt(0));
			listView.setSelectionFromTop(scrollPos, scrollTop);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		try {
			ListView listView = ((ListView) ((ViewGroup) adapter.getBodyView()).getChildAt(0));
			scrollPos = listView.getFirstVisiblePosition();
			scrollTop = listView.getChildAt(0) != null ? listView.getChildAt(0).getTop() : 0;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public ScrolledPullToRequestView(Context context) {
		super(context);
	}

	public ScrolledPullToRequestView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrolledPullToRequestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setAdapter(PullToRequestAdatper adapterTmp) {
		this.adapter = adapterTmp;
		super.setAdapter(adapterTmp);
	}
}
