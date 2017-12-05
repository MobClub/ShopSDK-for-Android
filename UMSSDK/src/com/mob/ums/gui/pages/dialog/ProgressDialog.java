package com.mob.ums.gui.pages.dialog;

import android.content.Context;

import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.Theme;

public class ProgressDialog extends Dialog<ProgressDialog> {
	
	public ProgressDialog(Context context, Theme theme) {
		super(context, theme);
	}
	
	public static class Builder extends Dialog.Builder<ProgressDialog> {
		
		public Builder(Context context, Theme theme) {
			super(context, theme);
			setCancelable(true);
		}
		
		protected ProgressDialog createDialog(Context context, Theme theme) {
			return new ProgressDialog(context, theme);
		}
	}
	
}
