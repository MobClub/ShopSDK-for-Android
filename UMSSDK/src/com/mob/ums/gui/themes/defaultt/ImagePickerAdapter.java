package com.mob.ums.gui.themes.defaultt;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.PhotoCropPage;
import com.mob.ums.gui.pickers.ImagePicker;

public class ImagePickerAdapter extends ScrollUpAndDownDialogAdapter<ImagePicker> implements OnClickListener {
	private ImagePicker dialog;
	
	public void init(ImagePicker dialog) {
		this.dialog = dialog;
		super.init(dialog);
	}
	
	protected void initBodyView(LinearLayout llBody) {
		Context context = dialog.getContext();
		llBody.setBackgroundColor(0);
		int dp10 = ResHelper.dipToPx(context, 10);
		llBody.setPadding(dp10, dp10, dp10, dp10);
		
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundResource(ResHelper.getBitmapRes(context, "umssdk_default_image_picker_bg"));
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(ll, lp);
		
		TextView tv = new TextView(context);
		tv.setId(1);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 18));
		tv.setTextColor(0xff3b3947);
		tv.setGravity(Gravity.CENTER);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_camera"));
		int dp45 = ResHelper.dipToPx(context, 43);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp45);
		ll.addView(tv, lp);
		tv.setOnClickListener(this);
		
		View vLine = new View(context);
		vLine.setBackgroundColor(0xffeeeeee);
		lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
		ll.addView(vLine, lp);
		
		tv = new TextView(context);
		tv.setId(2);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 18));
		tv.setTextColor(0xff3b3947);
		tv.setGravity(Gravity.CENTER);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_select_from_album"));
		lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp45);
		ll.addView(tv, lp);
		tv.setOnClickListener(this);
		
		tv = new TextView(context);
		tv.setId(3);
		tv.setBackgroundResource(ResHelper.getBitmapRes(context, "umssdk_default_image_picker_bg"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 18));
		tv.setTextColor(0xff3b3947);
		tv.setGravity(Gravity.CENTER);
		tv.setText(ResHelper.getStringRes(context, "umssdk_default_cancel"));
		lp = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp45);
		lp.topMargin = ResHelper.dipToPx(context, 15);
		llBody.addView(tv, lp);
		tv.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		if (v.getId() == 1) {
			PhotoCropPage page = new PhotoCropPage(dialog.getTheme());
			page.showForCamera(dialog.getOnImageGotListener());
		} else if (v.getId() == 2) {
			PhotoCropPage page = new PhotoCropPage(dialog.getTheme());
			page.showForAlbum(dialog.getOnImageGotListener());
		}
		dialog.dismiss();
	}
	
}
