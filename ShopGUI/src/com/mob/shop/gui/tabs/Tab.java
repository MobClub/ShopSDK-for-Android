package com.mob.shop.gui.tabs;

import android.content.Context;
import android.view.View;

public interface Tab {

	public String getSelectedIconResName();

	public String getUnselectedIconResName();

	public String getTitleResName();

	public String getSelectedTitleColor();

	public String getUnselectedTitleColor();

	public View getTabView(Context context);

	public void onSelected();

	public void onUnselected();

}
