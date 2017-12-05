package com.mob.ums.gui.pages.dialog;

import android.content.Context;

import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.Theme;

import java.util.HashMap;

public class OKDialog extends Dialog<OKDialog> {
	private String title;
	private String message;
	private String button;
	private boolean noPadding;

	public OKDialog(Context context, Theme theme) {
		super(context, theme);
	}

	protected void applyParams(HashMap<String, Object> params) {
		title = (String) params.get("title");
		message = (String) params.get("message");
		button = (String) params.get("button");
		noPadding = "true".equals(String.valueOf(params.get("noPadding")));
	}

	public String getTitle() {
		return title == null ? "" : title;
	}

	public String getMessage() {
		return message == null ? "" : message;
	}

	public String getButton() {
		return button == null ? "" : button;
	}

	public boolean isNoPadding() {
		return noPadding;
	}
	
	public static class Builder extends Dialog.Builder<OKDialog> {

		public Builder(Context context, Theme theme) {
			super(context, theme);
		}

		public synchronized void setTitle(String tvTitle) {
			set("title", tvTitle);
		}

		public synchronized void setMessage(String message) {
			set("message", message);
		}

		public synchronized void setButton(String text) {
			set("button", text);
		}
		
		public synchronized void noPadding() {
			set("noPadding", true);
		}
	}
}
