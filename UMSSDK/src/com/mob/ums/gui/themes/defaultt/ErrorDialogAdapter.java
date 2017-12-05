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
import com.mob.ums.gui.pages.dialog.ErrorDialog;

public class ErrorDialogAdapter extends DefaultDialogAdapter<ErrorDialog> {
	private TextView tvTitle;
	private TextView tvMessage;
	private TextView tvOK;

	public void init(final ErrorDialog dialog) {
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
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 1));
		containLayout.addView(vLine, lp);

		//btn
		tvOK = new TextView(context);
		tvOK.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 18));
		tvOK.setTextColor(0xffc4554c);
		tvOK.setGravity(Gravity.CENTER);
		tvOK.setBackgroundResource(ResHelper.getStringRes(context, "umssdk_default_dialog_button"));
		String btnText = dialog.getButton();
		if (TextUtils.isEmpty(btnText)) {
			tvOK.setText(ResHelper.getStringRes(context, "umssdk_default_confirm"));
		} else {
			tvOK.setText(btnText);
		}
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 43));
		containLayout.addView(tvOK, lp);
		tvOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

}
