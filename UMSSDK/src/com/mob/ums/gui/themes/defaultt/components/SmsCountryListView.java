package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

public class SmsCountryListView extends RelativeLayout implements OnTouchListener {
	private GroupListView lvContries;
	private TextView tvScroll;
	private LinearLayout llScroll;
	private SmsCountryListAdapter adapter;

	public SmsCountryListView(Context context) {
		super(context);
		init(context);
	}

	public SmsCountryListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SmsCountryListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context) {
		lvContries = new GroupListView(context);
		lvContries.setDividerHeight(ResHelper.dipToPx(context, 1));
		int resId = ResHelper.getBitmapRes(context, "umssdk_default_sms_country_list_sep");
		lvContries.setDivider(context.getResources().getDrawable(resId));
		adapter = new SmsCountryListAdapter(lvContries);
		lvContries.setAdapter(adapter);
		LayoutParams lpContries = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(lvContries, lpContries);

		tvScroll = new TextView(context);
		tvScroll.setTextColor(0xff3b3945);
		resId = ResHelper.getBitmapRes(context, "smssdk_country_group_scroll_down");
		if (resId > 0) {
			tvScroll.setBackgroundResource(resId);
		}
		tvScroll.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 40));
		tvScroll.setTypeface(Typeface.DEFAULT);
		tvScroll.setVisibility(GONE);
		tvScroll.setGravity(Gravity.CENTER);
		int dp60 = ResHelper.dipToPx(context, 60);
		LayoutParams lp = new LayoutParams(dp60, dp60);
		lp.addRule(CENTER_IN_PARENT);
		addView(tvScroll, lp);

		llScroll = new LinearLayout(context);
		resId = ResHelper.getBitmapRes(context, "smssdk_country_group_scroll_up");
		if (resId > 0) {
			llScroll.setBackgroundResource(resId);
		}
		llScroll.setOrientation(LinearLayout.VERTICAL);
		llScroll.setOnTouchListener(this);
		lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_RIGHT);
		lp.addRule(CENTER_VERTICAL);
		lp.rightMargin = ResHelper.dipToPx(context, 8);
		lp.topMargin = ResHelper.dipToPx(context, 22);
		lp.bottomMargin = ResHelper.dipToPx(context, 55);
		addView(llScroll, lp);

		initScroll(context);
	}

	public void initSearchEngine() {
		adapter.initSearchEngine();
	}

	private void initScroll(Context context) {
		llScroll.removeAllViews();

		int size = adapter.getGroupCount();
		int dp3 = ResHelper.dipToPx(context, 3);
		int dp2 = ResHelper.dipToPx(context, 2);
		for (int i = 0; i < size; i++) {
			TextView tv = new TextView(context);
			tv.setText(adapter.getGroupTitle(i));
			tv.setTextColor(0xff3b3945);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 12));
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(dp3, dp2, dp3, dp2);
			llScroll.addView(tv);
		}
	}

	/**
	 * 设置列表右边的字母索引的滑动监听
	 */
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				int resId = ResHelper.getBitmapRes(v.getContext(), "smssdk_country_group_scroll_down");
				if (resId > 0) {
					v.setBackgroundResource(resId);
				}
				float x = event.getX();
				float y = event.getY();
				ViewGroup vg = (ViewGroup) v;
				onScroll(vg, x, y);
			} break;
			case MotionEvent.ACTION_MOVE: {
				float x = event.getX();
				float y = event.getY();
				ViewGroup vg = (ViewGroup) v;
				onScroll(vg, x, y);
			} break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				int resId = ResHelper.getBitmapRes(v.getContext(), "smssdk_country_group_scroll_up");
				if (resId > 0) {
					v.setBackgroundResource(resId);
				}
				tvScroll.setVisibility(GONE);
			} break;
		}
		return true;
	}

	/**
	 * 设置列表右边的字母索引的滑动时的显示
	 */
	public void onScroll(ViewGroup llScroll, float x, float y) {
		for (int i = 0, count = llScroll.getChildCount(); i < count; i++) {
			TextView v = (TextView) llScroll.getChildAt(i);
			if (x >= v.getLeft() && x <= v.getRight()
					&& y >= v.getTop() && y <= v.getBottom()) {
				lvContries.setSelection(i);
				tvScroll.setVisibility(VISIBLE);
				tvScroll.setText(v.getText());
				break;
			}
		}
	}

	/**
	 * 搜索接口
	 *
	 * @param token
	 */
	public void onSearch(String token) {
		adapter.search(token);
		adapter.notifyDataSetChanged();
		if (adapter.getGroupCount() == 0) {
			llScroll.setVisibility(View.GONE);
		} else {
			llScroll.setVisibility(View.VISIBLE);
		}
		initScroll(getContext());
	}

	/**
	 * 设置listview item 的点击事件监听
	 *
	 * @param listener
	 */
	public void setOnItemClickListener(GroupListView.OnItemClickListener listener) {
		lvContries.setOnItemClickListener(listener);
	}

	/**
	 * 获取国家对象<国家名，区号， ID>
	 *
	 * @param group
	 * @param position
	 * @return
	 */
	public String[] getCountry(int group, int position) {
		return adapter.getItem(group, position);
	}

}