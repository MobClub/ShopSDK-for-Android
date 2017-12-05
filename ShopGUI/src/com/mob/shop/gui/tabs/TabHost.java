package com.mob.shop.gui.tabs;

import android.content.Context;
import android.view.View;

public interface TabHost {

	public void addTab(Tab tab);

	public void setSelection(int selection);

	public View getHostView(Context context);

}
