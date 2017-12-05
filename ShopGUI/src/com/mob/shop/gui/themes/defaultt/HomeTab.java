package com.mob.shop.gui.themes.defaultt;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.mob.shop.biz.ShopLog;
import com.mob.shop.gui.R;
import com.mob.shop.gui.tabs.Tab;

public class HomeTab implements Tab {
	private static final String TAG = "HomeTab";

	@Override
	public String getSelectedIconResName() {
		return "shopsdk_default_orange_home";
	}

	@Override
	public String getUnselectedIconResName() {
		return "shopsdk_default_grey_home";
	}

	@Override
	public String getTitleResName() {
		return "shopsdk_default_home";
	}

	@Override
	public String getSelectedTitleColor() {
		return "select_tab";
	}

	@Override
	public String getUnselectedTitleColor() {
		return "unselect_tab";
	}

	@Override
	public View getTabView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_tab_home, null);
		return view;
	}

	@Override
	public void onSelected() {
		ShopLog.getInstance().d(TAG,"onSelected");
	}

	@Override
	public void onUnselected() {
		ShopLog.getInstance().d(TAG,"onUnselected");
	}
}
