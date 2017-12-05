package com.mob.shop.gui.themes.defaultt.components.searchbar;

import android.content.Context;
import android.util.AttributeSet;

import com.mob.shop.gui.R;

/**
 * Created by yjin on 2017/9/19.
 */

public class MainSearchBar extends TransformingToolbar {
	public MainSearchBar(Context context) {
		super(context);
		setBackground(context.getResources().getDrawable(R.drawable.shopsdk_default_good_search_bar_bg));
	}

	public MainSearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setBackground(context.getResources().getDrawable(R.drawable.shopsdk_default_good_search_bar_bg));

	}
	public MainSearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackground(context.getResources().getDrawable(R.drawable.shopsdk_default_good_search_bar_bg));
	}

}
