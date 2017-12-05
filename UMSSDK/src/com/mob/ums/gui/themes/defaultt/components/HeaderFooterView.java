package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

public abstract class HeaderFooterView extends RelativeLayout {
	private TextView tvHeader;
	private ImageView ivArrow;
	private ProgressBar pbRefreshing;
	
	public HeaderFooterView(Context context) {
		super(context);
		
		LinearLayout ll = new LinearLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(ll, lp);
		
		ivArrow = new ImageView(context);
		ivArrow.setScaleType(ScaleType.CENTER_INSIDE);
		int resId = ResHelper.getBitmapRes(context, getPullArrowResName());
		if (resId > 0) {
			ivArrow.setImageResource(resId);
		}
		int dp20 = ResHelper.dipToPx(context, 26);
		LinearLayout.LayoutParams lpll = new LinearLayout.LayoutParams(dp20, dp20);
		int dp5 = ResHelper.dipToPx(context, 5);
		lpll.rightMargin = dp5;
		lpll.topMargin = dp5;
		lpll.bottomMargin = dp5;
		ll.addView(ivArrow, lpll);

		int dp18 = ResHelper.dipToPx(context, 18);
		pbRefreshing = new ProgressBar(context);
		lpll = new LinearLayout.LayoutParams(dp18, dp18);
		lpll.rightMargin = dp5;
		lpll.topMargin = dp5;
		lpll.bottomMargin = dp5;
		ll.addView(pbRefreshing, lpll);
		resId=ResHelper.getBitmapRes(context,"umssdk_default_progress_bar_loading");
		pbRefreshing.setIndeterminateDrawable(context.getResources().getDrawable(resId));
		pbRefreshing.setVisibility(View.GONE);
		
		tvHeader = new TextView(getContext());
		tvHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tvHeader.setTextColor(getTextColor());
		tvHeader.setGravity(Gravity.CENTER);
		int dp30 = ResHelper.dipToPx(context, 30);
		lpll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, dp30);
		lpll.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(tvHeader, lpll);
	}
	
	public void onPull(int percent) {
		ivArrow.setVisibility(View.VISIBLE);
		int resId;
		if (percent < 100) {
			resId = ResHelper.getBitmapRes(getContext(), getPullArrowResName());
		} else {
			resId = ResHelper.getBitmapRes(getContext(), getReleaseArrowResName());
		}
		if (resId > 0) {
			ivArrow.setImageResource(resId);
		}
		
		if (percent < 100) {
			resId = ResHelper.getStringRes(getContext(), getPullMessageResName());
			if (resId > 0) {
				tvHeader.setText(resId);
			}
		} else {
			resId = ResHelper.getStringRes(getContext(), getReleaseMessageResName());
			if (resId > 0) {
				tvHeader.setText(resId);
			}
		}
	}
	
	public void onRequest() {
		ivArrow.setVisibility(View.GONE);
		pbRefreshing.setVisibility(View.VISIBLE);
		int resId = ResHelper.getStringRes(getContext(), getLoadingMessageResName());
		if (resId > 0) {
			tvHeader.setText(resId);
		}
	}
	
	public void reverse() {
		ivArrow.setVisibility(View.VISIBLE);
		pbRefreshing.setVisibility(View.GONE);
		int resId = ResHelper.getBitmapRes(getContext(), getPullArrowResName());
		if (resId > 0) {
			ivArrow.setImageResource(resId);
		}
	}
	
	protected abstract String getPullArrowResName();
	
	protected abstract String getReleaseArrowResName();
	
	protected abstract String getPullMessageResName();
	
	protected abstract String getReleaseMessageResName();
	
	protected abstract String getLoadingMessageResName();
	
	protected abstract int getTextColor();
}
