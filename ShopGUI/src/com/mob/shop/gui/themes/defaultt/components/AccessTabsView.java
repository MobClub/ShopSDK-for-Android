package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.shop.gui.R;
import com.mob.tools.utils.ResHelper;


public class AccessTabsView extends FrameLayout {
	private int cursorHeight = 2;
	private LinearLayout ll;
	private FrameLayout cfl;
	private TextView cursor;
	private int currentPosition;
	private int itemWidth;
	private TextView[] tabs;
	private TextView[] subTabs;
	private OnTabClickListener onTabClickListener;
	private Context context;

	public AccessTabsView(Context context) {
		this(context, null);
	}

	public AccessTabsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AccessTabsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		setBackgroundColor(0xFFFFFFFF);

		ll = new LinearLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setLayoutParams(lp);

		View greyLine = new View(context);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 1));
		lp.gravity = Gravity.BOTTOM;
		greyLine.setBackgroundResource(R.color.grey_line);
		greyLine.setLayoutParams(lp);
		addView(greyLine);

		cfl = new FrameLayout(context);
		cfl.setBackgroundResource(R.color.transparent);
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

	public void setDataRes(String[] resNames, int[] sTabs) {
		setData(resNames, sTabs, true);
	}

	public void setData(String[] datas, int[] sTabs) {
		setData(datas, sTabs, false);
	}

	private void setData(String[] data, int[] sTabs, boolean isRes) {
		int num = data.length;
		int screenWidth = ResHelper.getScreenWidth(context);
		itemWidth = screenWidth / num;
		tabs = new TextView[num];
		subTabs = new TextView[num];
		String format = context.getString(ResHelper.getStringRes(context, "shopsdk_default_apprise_count"));
		for (int i = 0; i < num; i++) {
			FrameLayout fl = new FrameLayout(context);
			LayoutParams lp = new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT);
			lp.gravity = Gravity.CENTER;
			fl.setLayoutParams(lp);

			LinearLayout lll = new LinearLayout(context);
			lll.setOrientation(LinearLayout.VERTICAL);
			LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp1.gravity = Gravity.CENTER;
			lll.setLayoutParams(lp1);

			TextView tv = new TextView(context);
			if (isRes) {
				tv.setText(context.getResources().getString(ResHelper.getStringRes(context, data[i])));
			} else {
				tv.setText(data[i]);
			}
			tv.setGravity(Gravity.CENTER_HORIZONTAL);

			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
			tv.setTextColor(0xFF666666);
			tabs[i] = tv;

			lll.addView(tv);

			TextView tvCount = new TextView(context);
			tvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
			tvCount.setTextColor(0xFF666666);
			tvCount.setGravity(Gravity.CENTER_HORIZONTAL);
			subTabs[i] = tvCount;
			if (subTabs != null) {
				tvCount.setText(String.format(format, 0));
			}

			lll.addView(tvCount);
			fl.addView(lll);
			ll.addView(fl);
			final int finalI = i;
			lll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					changeTab(finalI);
					onTabClickListener.onTabClick(finalI);
				}
			});
		}
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
		lp.width = itemWidth;
		cfl.setLayoutParams(lp);

		lp = (LayoutParams) cursor.getLayoutParams();
		lp.width = itemWidth-ResHelper.dipToPx(context,20);

		tabs[0].setTextColor(0xFFf6573b);
		subTabs[0].setTextColor(0xFFf6573b);
	}

	public void setSubTab(int[] sTabs) {
		String format = context.getString(ResHelper.getStringRes(context, "shopsdk_default_apprise_count"));
		for (int i = 0; i < subTabs.length; i++) {
			subTabs[i].setText(String.format(format, sTabs[i]));
		}
	}

	public void changeTab(int change) {
		if (currentPosition == change) {
			return;
		}

		tabs[change].setTextColor(0xFFf6573b);
		tabs[currentPosition].setTextColor(0xFF666666);
		subTabs[change].setTextColor(0xFFf6573b);
		subTabs[currentPosition].setTextColor(0xFF666666);

		changeTabAnim(change);
	}

	private void changeTabAnim(int selectTab) {
		TranslateAnimation animation = new TranslateAnimation(itemWidth * currentPosition, itemWidth * selectTab, 0,
				0);
		currentPosition = selectTab;
		animation.setFillAfter(true);
		animation.setDuration(300);
		cfl.startAnimation(animation);
	}

	public float getTextWidth(TextView tv) {
		TextPaint paint = tv.getPaint();

		return paint.measureText(tv.getText().toString().trim());
	}

	public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
		this.onTabClickListener = onTabClickListener;
	}

	public interface OnTabClickListener {
		void onTabClick(int index);
	}
}
