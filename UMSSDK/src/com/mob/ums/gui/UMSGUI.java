package com.mob.ums.gui;

import com.mob.MobSDK;
import com.mob.jimu.gui.Theme;
import com.mob.tools.utils.ReflectHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.User;
import com.mob.ums.gui.pages.LoginPage;
import com.mob.ums.gui.themes.defaultt.DefaultTheme;

/** gui main class */
public class UMSGUI {
	private static Theme theme;

	/** set theme */
	public static <T extends Theme> void setTheme(Class<T> theme) {
		try {
			UMSGUI.theme = theme.newInstance();
		} catch(Throwable t) {
//			t.printStackTrace();
		}
	}
	
	/** show login page */
	public static void showLogin(OperationCallback<User> callback) {
		if (theme == null) {
			setTheme(DefaultTheme.class);
		}
		LoginPage page = new LoginPage(theme);
		page.setResultCallback(callback);
		page.show(MobSDK.getContext(), null);
	}

	public static boolean isDebug() {
		try {
			String clzName = MobSDK.getContext().getPackageName() + ".BuildConfig";
			clzName = ReflectHelper.importClass(clzName);
			return (Boolean) ReflectHelper.getStaticField(clzName, "DEBUG");
		} catch (Throwable t) {}
		return false;
	}

}
