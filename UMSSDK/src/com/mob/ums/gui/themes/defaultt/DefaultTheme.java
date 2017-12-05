package com.mob.ums.gui.themes.defaultt;

import android.content.Context;

import com.mob.jimu.gui.DialogAdapter;
import com.mob.jimu.gui.PageAdapter;
import com.mob.jimu.gui.Theme;
import com.mob.tools.utils.ResHelper;

import java.util.Set;

public class DefaultTheme extends Theme {
	
	protected void initPageAdapters(Set<Class<? extends PageAdapter<?>>> adapters) {
		adapters.add(ChangePasswordPageAdapter.class);
		adapters.add(CountryCodeSelectorePageAdapter.class);
		adapters.add(LoginPageAdapter.class);
		adapters.add(RegisterPageAdapter.class);
		adapters.add(TextPickerAdapter.class);
		adapters.add(PhotoCropPageAdapter.class);
		adapters.add(PostRegisterPageAdapter.class);

	}
	
	protected void initDialogAdapters(Set<Class<? extends DialogAdapter<?>>> adapters) {
		adapters.add(ImagePickerAdapter.class);
		adapters.add(SingleValuePickerAdapter.class);
		adapters.add(OKDialogAdapter.class);
		adapters.add(OKCancelDialogAdapter.class);
		adapters.add(ProgressDialogAdapter.class);
		adapters.add(ErrorDialogAdapter.class);
	}

	public int getDialogStyle(Context context) {
		return ResHelper.getStyleRes(context, "umssdk_default_DialogStyle");
	}

}
