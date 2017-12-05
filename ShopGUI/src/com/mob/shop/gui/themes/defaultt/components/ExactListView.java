package com.mob.shop.gui.themes.defaultt.components;

import android.widget.GridView;
import android.widget.ListView;

public class ExactListView extends ListView {
	public ExactListView(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
