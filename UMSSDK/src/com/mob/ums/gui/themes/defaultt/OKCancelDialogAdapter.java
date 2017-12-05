package com.mob.ums.gui.themes.defaultt;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.dialog.OKCancelDialog;

public class OKCancelDialogAdapter extends DefaultDialogAdapter<OKCancelDialog> {
	private TextView tvTitle;
	private TextView tvMessage;
	private TextView tvOK;
	private TextView tvCancel;

	public void init(final OKCancelDialog dialog) {
		Context context = dialog.getContext();

		//setup view
		LinearLayout containLayout = new LinearLayout(context);
		containLayout.setBackgroundResource(ResHelper.getBitmapRes(context, "umssdk_default_dialog_prompt"));
		containLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setContentView(containLayout, lp);

		//title
		tvTitle = new TextView(context);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvTitle.setTextColor(0xFF111111);
		tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		tvTitle.setText(dialog.getTitle());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 25));
		int dp13 = ResHelper.dipToPx(context, 13);
		lp.topMargin = dp13;
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		containLayout.addView(tvTitle, lp);

		//content msg
		tvMessage = new TextView(context);
		if (!dialog.isNoPadding()) {
			int paddingMsg = ResHelper.dipToPx(context, 50);
			tvMessage.setPadding(paddingMsg, 0, paddingMsg, 0);
		}
		tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp13);
		tvMessage.setTextColor(0xff000000);
		tvMessage.setMaxLines(2);
		tvMessage.setGravity(Gravity.CENTER);
		tvMessage.setText(dialog.getMessage());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 40));
		lp.bottomMargin = dp13;
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		containLayout.addView(tvMessage, lp);

		//line
		View vLine = new View(context);
		vLine.setBackgroundColor(0x7f979797);
		int dp1 = ResHelper.dipToPx(context, 1);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp1);
		containLayout.addView(vLine, lp);

		LinearLayout llBottom = new LinearLayout(context);
		llBottom.setBackgroundResource(ResHelper.getStringRes(context, "umssdk_default_dialog_button"));
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 43));
		containLayout.addView(llBottom, lp);

		//btnCancel
		tvCancel = new TextView(context);
		tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvCancel.setTextColor(0xffc4554c);
		tvCancel.setGravity(Gravity.CENTER);
		tvCancel.setBackgroundResource(ResHelper.getStringRes(context, "umssdk_default_dialog_btn_left"));
		String lbl = dialog.getButtonCancel();
		if (TextUtils.isEmpty(lbl)) {
			tvCancel.setText(ResHelper.getStringRes(context, "umssdk_default_cancel"));
		} else {
			tvCancel.setText(lbl);
		}
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBottom.addView(tvCancel, lp);
		tvCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.setNegative();
			}
		});

		vLine = new View(context);
		vLine.setBackgroundColor(0x7f979797);
		lp = new LayoutParams(dp1, LayoutParams.MATCH_PARENT);
		llBottom.addView(vLine, lp);

		//btnOK
		tvOK = new TextView(context);
		tvOK.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvOK.setTextColor(0xffc4554c);
		tvOK.setGravity(Gravity.CENTER);
		tvOK.setBackgroundResource(ResHelper.getStringRes(context, "umssdk_default_dialog_btn_right"));
		String btnText = dialog.getButtonOK();
		if (TextUtils.isEmpty(btnText)) {
			tvOK.setText(ResHelper.getStringRes(context, "umssdk_default_confirm"));
		} else {
			tvOK.setText(btnText);
		}
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		llBottom.addView(tvOK, lp);
		tvOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.setPositive();
			}
		});
	}

}
