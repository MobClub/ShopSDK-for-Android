package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.gui.UMSGUI;

public class RegisterPage extends Page<RegisterPage> {
	private int type;
	private String loginNumber;

	public RegisterPage(Theme theme) {
		super(theme);
	}
	
	public void onCreate() {
		if (!UMSGUI.isDebug()) {
			disableScreenCapture();
		}
		super.onCreate();
	}
	
	public void setLoginNumber(String loginNumber) {
		this.loginNumber = loginNumber;
	}

	public String getLoginNumber() {
		return loginNumber;
	}

	public void setForgetPasswrodType() {
		type = 1;
	}
	
	public void setBindPhoneType() {
		type = 2;
	}

	public boolean isForgetPasswrodType() {
		return 1 == type;
	}
	
	public boolean isBindPhoneType() {
		return 2 == type;
	}

}
