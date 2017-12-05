package com.mob.ums.gui.themes.defaultt;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.dialog.ProgressDialog;

public class ProgressDialogAdapter extends DefaultDialogAdapter<ProgressDialog>  {
	
	public void init(ProgressDialog dialog) {
		Context context = dialog.getContext();
		FrameLayout fl = new FrameLayout(context);
		LayoutParams lpfl = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setContentView(fl, lpfl);
		
		ProgressBar pb = new ProgressBar(context);
		int dp10 = ResHelper.dipToPx(context, 10);
		pb.setPadding(dp10, dp10, dp10, dp10);
		int dp80 = ResHelper.dipToPx(context, 80);
		lpfl = new LayoutParams(dp80, dp80);
		lpfl.gravity = Gravity.CENTER;
		fl.addView(pb);
	}
	
}
