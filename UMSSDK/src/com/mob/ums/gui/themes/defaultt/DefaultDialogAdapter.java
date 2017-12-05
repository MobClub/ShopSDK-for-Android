package com.mob.ums.gui.themes.defaultt;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.DialogAdapter;
import com.mob.tools.utils.ResHelper;

public class DefaultDialogAdapter<D extends Dialog<D>> extends DialogAdapter<D> {

	public void onCreate(D dialog, Bundle savedInstanceState) {
		Context context = dialog.getContext();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams wlp = window.getAttributes();
		wlp.gravity = Gravity.CENTER;
		int dp50 = ResHelper.dipToPx(context, 50);
		wlp.width = context.getResources().getDisplayMetrics().widthPixels - dp50 * 2;
	}

}
