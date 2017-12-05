package com.mob.ums.gui.pages.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.Theme;

import java.util.HashMap;

public class OKCancelDialog extends Dialog<OKCancelDialog> {
	private OnClickListener ocl;
	private String title;
	private String message;
	private String buttonOk;
	private String buttonCancel;
	private boolean noPadding;

	public OKCancelDialog(Context context, Theme theme) {
		super(context, theme);
	}

	protected void applyParams(HashMap<String, Object> params) {
		title = (String) params.get("title");
		message = (String) params.get("message");
		buttonOk = (String) params.get("buttonOk");
		buttonCancel = (String) params.get("buttonCancel");
		noPadding = "true".equals(String.valueOf(params.get("noPadding")));
	}

	public String getTitle() {
		return title == null ? "" : title;
	}

	public String getMessage() {
		return message == null ? "" : message;
	}

	public String getButtonOK() {
		return buttonOk == null ? "" : buttonOk;
	}

	public String getButtonCancel() {
		return buttonCancel == null ? "" : buttonCancel;
	}
	
	public boolean isNoPadding() {
		return noPadding;
	}
	
	public void setPositive() {
		if (ocl != null) {
			ocl.onClick(this, DialogInterface.BUTTON_POSITIVE);
		}
	}

	public void setNegative() {
		if (ocl != null) {
			ocl.onClick(this, DialogInterface.BUTTON_NEGATIVE);
		}
	}

	public static class Builder extends Dialog.Builder<OKCancelDialog> {
		private OnClickListener ocl;

		public Builder(Context context, Theme theme) {
			super(context, theme);
		}

		protected OKCancelDialog createDialog(Context context, Theme theme) {
			return new OKCancelDialog(context, theme);
		}

		public synchronized void setTitle(String tvTitle) {
			set("title", tvTitle);
		}

		public synchronized void setMessage(String message) {
			set("message", message);
		}

		public synchronized void setButtonOK(String text) {
			set("buttonOk", text);
		}

		public synchronized void setButtonCancel(String text) {
			set("buttonCancel", text);
		}

		public void setOnClickListener(OnClickListener ocl) {
			this.ocl = ocl;
		}
		
		public synchronized void noPadding() {
			set("noPadding", true);
		}
		
		public OKCancelDialog create() throws Throwable {
			OKCancelDialog d = super.create();
			d.ocl = ocl;
			return d;
		}
	}
}
