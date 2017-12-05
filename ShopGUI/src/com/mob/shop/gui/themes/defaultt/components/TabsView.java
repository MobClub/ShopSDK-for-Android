package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.paysdk.beans.TicketData;
import com.mob.tools.utils.ResHelper;

public class TabsView extends FrameLayout {
	private int cursorHeight = 2;
	private LinearLayout ll;
	private FrameLayout cfl;
	private TextView cursor;
	private int currentPosition;
	private int itemWidth;
	private TextView[] tabs;
	private OnTabClickListener onTabClickListener;

	public TabsView(Context context) {
		this(context, null);
	}

	public TabsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TabsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		setBackgroundColor(0xFFFFFFFF);

		ll = new LinearLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(lp);

		View greyLine = new View(context);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 1));
		lp.gravity = Gravity.BOTTOM;
		greyLine.setBackgroundResource(ResHelper.getColorRes(context,"grey_line"));
		greyLine.setLayoutParams(lp);
		addView(greyLine);

		cfl = new FrameLayout(context);
		cfl.setBackgroundResource(ResHelper.getColorRes(context,"transparent"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM;
		cfl.setLayoutParams(lp);

		cursor = new TextView(context);
		cursor.setBackgroundColor(0xffFF5733);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, ResHelper.dipToPx(context, cursorHeight));
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		cursor.setLayoutParams(lp);
		cfl.addView(cursor);

		addView(cfl);
		addView(ll);

	}

	public void setDataRes(Context context, String[] resNames) {

		setData(context, resNames, true);
	}

	public void setData(Context context, String[] datas) {
		setData(context, datas, false);
	}

	private void setData(final Context context, String[] data, boolean isRes) {
		int num = data.length;
		int screenWidth = ResHelper.getScreenWidth(context);
		itemWidth = screenWidth / num;
		tabs = new TextView[num];
		for (int i = 0; i < num; i++) {
			FrameLayout fl = new FrameLayout(context);
			LayoutParams lp = new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT);
			lp.gravity = Gravity.CENTER;
			fl.setLayoutParams(lp);

			TextView tv = new TextView(context);
			if (isRes) {
				tv.setText(context.getResources().getString(ResHelper.getStringRes(context, data[i])));
			} else {
				tv.setText(data[i]);
			}

			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			tv.setTextColor(0xFF666666);
			tv.setGravity(Gravity.CENTER);
			tabs[i] = tv;

			fl.addView(tv);
			ll.addView(fl);
			final int finalI = i;
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					changeTab(finalI);
					if (onTabClickListener != null) {
						onTabClickListener.onTabClick(finalI);
					}
				}
			});
		}
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
		lp.width = itemWidth;
		cfl.setLayoutParams(lp);

		lp = (LayoutParams) cursor.getLayoutParams();
		lp.width = (int) getTextWidth(tabs[0]);

		tabs[0].setTextColor(0xFFf6573b);
	}

	public void changeTab(int change) {
		if (currentPosition == change) {
			return;
		}

		tabs[change].setTextColor(0xFFf6573b);
		tabs[currentPosition].setTextColor(0xFF666666);

		float width = getTextWidth(tabs[change]);
		float currentWidth = getTextWidth(tabs[currentPosition]);

		changeTabAnim(change, currentWidth, width);
	}

	private void changeTabAnim(int selectTab, float currentWidth, float width) {
		TranslateAnimation animation = new TranslateAnimation(itemWidth * currentPosition, itemWidth * selectTab, 0,
				0);
		currentPosition = selectTab;
		animation.setFillAfter(true);
		animation.setDuration(300);

		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, width / currentWidth, 1f, 1f, Animation
				.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setFillAfter(true);
		scaleAnimation.setDuration(300);
		cfl.startAnimation(animation);

		if (width != currentWidth) {
			cursor.startAnimation(scaleAnimation);
		}
	}

	public float getTextWidth(TextView tv) {
		TextPaint paint = tv.getPaint();

		return paint.measureText(tv.getText().toString().trim());
	}

	public int getCurrentPosition() {
		return this.currentPosition;
	}

	public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
		this.onTabClickListener = onTabClickListener;
	}

	public void performClick(int tabIndex) {
		if (tabs != null && tabIndex <= tabs.length) {
			tabs[tabIndex].performClick();
		}
	}

	public interface OnTabClickListener {
		void onTabClick(int index);
	}
}
