package com.mob.ums.gui.pages;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;

/** login page */
public class LoginPage extends Page<LoginPage> {
	private static LoginPage instance;
	private OperationCallback<User> callback;
	private User me;

	public LoginPage(Theme theme) {
		super(theme);
	}

	public void onCreate() {
		if (!UMSGUI.isDebug()) {
			disableScreenCapture();
		}
		super.onCreate();
		instance = this;
	}
	
	public void setResultCallback(OperationCallback<User> callback) {
		this.callback = callback;
	}
	
	public boolean onFinish() {
		if (callback != null) {
			if (me == null) {
				callback.onCancel();
			} else {
				callback.onSuccess(me);
			}
		}
		return super.onFinish();
	}
	
	public void finishOnSuccess(User me) {
		this.me = me;
		finish();
	}
	
	public void onDestroy() {
		super.onDestroy();
		instance = null;
	}
	
	public static boolean isShown() {
		return instance != null;
	}
}
