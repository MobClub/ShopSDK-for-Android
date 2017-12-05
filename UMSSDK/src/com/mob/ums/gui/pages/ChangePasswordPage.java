package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.gui.UMSGUI;

public class ChangePasswordPage extends Page<ChangePasswordPage> {

	public ChangePasswordPage(Theme theme) {
		super(theme);
	}
	
	public void onCreate() {
		if (!UMSGUI.isDebug()) {
			disableScreenCapture();
		}
		super.onCreate();
	}
	
}
