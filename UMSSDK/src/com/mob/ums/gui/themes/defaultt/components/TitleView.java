package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

public abstract class TitleView extends LinearLayout {
	private ImageView ivReturn;
	private TextView tvRight;

	public TitleView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		RelativeLayout rl = new RelativeLayout(context);
		int dp43 = ResHelper.dipToPx(context, 40);
		int dp5 = ResHelper.dipToPx(context, 5);
		if (isNoPadding()) {
			addView(rl, new LayoutParams(LayoutParams.MATCH_PARENT, dp43 + 1));
		} else {
			addView(rl, new LayoutParams(LayoutParams.MATCH_PARENT, dp43 + dp5 + 1));
		}

		ivReturn = new ImageView(context);
		ivReturn.setId(1);
		ivReturn.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_return"));
		ivReturn.setScaleType(ScaleType.CENTER_INSIDE);
		int dp15 = ResHelper.dipToPx(context, 15);
		int dp32 = ResHelper.dipToPx(context, 32);
		ivReturn.setPadding(dp15, 0, dp32, 0);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(dp43 + dp15, dp43);
		rl.addView(ivReturn, lp);
		ivReturn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onReturn();
			}
		});

		TextView tv = new TextView(context);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		tv.setTextColor(0xff3b3947);
		tv.setText(ResHelper.getStringRes(context, getTitleResName()));
		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, dp43);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(tv, lp);

		if (getRightLabelResName() != null) {
			tvRight = new TextView(context);
			tvRight.setMinWidth(dp43 + dp15);
			tvRight.setPadding(0, 0, dp15, 0);
			tvRight.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			tvRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			tvRight.setTextColor(0xff3b3947);
			lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, dp43);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rl.addView(tvRight, lp);
			tvRight.setText(ResHelper.getStringRes(context, getRightLabelResName()));
			tvRight.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onRightClick();
				}
			});
		}

		View vSep = new View(context);
		vSep.setBackgroundColor(0xffe5e5e5);
		lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		lp.addRule(RelativeLayout.BELOW, ivReturn.getId());
		if (!isNoPadding()) {
			lp.bottomMargin = dp5;
		}
		rl.addView(vSep, lp);
	}

	public void disableReturn() {
		ivReturn.setOnClickListener(null);
		ivReturn.setVisibility(INVISIBLE);
	}

	protected void onReturn() {

	}

	protected abstract String getTitleResName();

	protected String getRightLabelResName() {
		return null;
	}

	protected void onRightClick() {

	}
	
	protected boolean isNoPadding() {
		return false;
	}
}
