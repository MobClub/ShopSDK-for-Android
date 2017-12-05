package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.PageAdapter;
import com.mob.tools.utils.ResHelper;


public class DefaultThemePageAdapter<P extends Page<P>> extends PageAdapter<P> {
	public void onCreate(P page, Activity activity) {
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0xffffffff));
	}

	public int getColor(String resName) {
		return getPage().getContext().getResources().getColor(ResHelper.getColorRes(getPage().getContext(), resName));
	}

	public int getBitmap(String resName) {
		return ResHelper.getBitmapRes(getPage().getContext(), resName);
	}

	public String getString(String resName) {
		return getPage().getContext().getResources().getString(ResHelper.getStringRes(getPage().getContext(),
				resName));
	}
}
