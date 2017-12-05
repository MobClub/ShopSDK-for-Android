package com.mob.shop.gui.pickers;

import android.content.Context;
import android.graphics.Bitmap;


import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.gui.base.Dialog;
import com.mob.shop.gui.base.Theme;

import java.util.HashMap;

public class ImagePicker extends Dialog<ImagePicker> {
	public OnImageGotListener listner;

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
		void onOmageGot(Bitmap bm);
		void onImageUploadSuccess(String id, String url);
		void onImageUploadFailed();
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
