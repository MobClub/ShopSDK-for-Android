package com.mob.ums.gui.pickers;

import android.content.Context;

import com.mob.jimu.gui.Dialog;
import com.mob.jimu.gui.Theme;

import java.util.HashMap;

public class ImagePicker extends Dialog<ImagePicker> {
	private OnImageGotListener listner;

	public ImagePicker(Context context, Theme theme) {
		super(context, theme);
	}
	
	public Theme getTheme() {
		return super.getTheme();
	}
	
	protected void applyParams(HashMap<String, Object> params) {
		listner = (OnImageGotListener) params.get("OnImageGotListener");
	}
	
	public OnImageGotListener getOnImageGotListener() {
		return listner;
	}
	
	public interface OnImageGotListener {
		public void onOmageGot(String id, String[] url);
	}
	
	public static class Builder extends Dialog.Builder<ImagePicker> {
		
		public Builder(Context context, Theme theme) {
			super(context, theme);
		}
		
		protected ImagePicker createDialog(Context context, Theme theme) {
			return new ImagePicker(context, theme);
		}
		
		public void setOnImageGotListener(OnImageGotListener l) {
			set("OnImageGotListener", l);
		}
	}

}
