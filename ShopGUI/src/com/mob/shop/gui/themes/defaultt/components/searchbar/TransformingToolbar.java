package com.mob.shop.gui.themes.defaultt.components.searchbar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by yjin on 2017/9/19.
 */

public class TransformingToolbar extends Toolbar {

	public TransformingToolbar(Context context) {
		super(context);
	}

	public TransformingToolbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TransformingToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * Sets the Visibility of all children to GONE
	 */
	public void hideContent() {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setVisibility(GONE);
		}
	}

	/**
	 * Sets the Visibility of all children to VISIBLE
	 */
	public void showContent() {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).setVisibility(VISIBLE);
		}
	}
}
