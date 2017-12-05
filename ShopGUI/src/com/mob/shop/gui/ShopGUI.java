package com.mob.shop.gui;

import com.mob.MobSDK;
import com.mob.shop.gui.base.Theme;
import com.mob.shop.gui.pages.MainPage;
import com.mob.shop.gui.themes.defaultt.DefaultTheme;
import com.mob.tools.utils.ReflectHelper;


/** gui main class */
public class ShopGUI {
	private static Theme theme;

	/** set theme */
	public static <T extends Theme> void setTheme(Class<T> theme) {
		try {
			ShopGUI.theme = theme.newInstance();
		} catch(Throwable t) {
//			t.printStackTrace();
		}
	}

	/** show shop page */
	public static void showShopPage(Callback callback) {
		if (theme == null) {
			setTheme(DefaultTheme.class);
		}
		MainPage page = new MainPage(theme);
		page.setCallback(callback);
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
