package com.mob.shop.gui.themes.defaultt.components;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.gui.PullToRequestListAdapter;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.gui.Scrollable;
import com.mob.tools.utils.ResHelper;

public abstract class DefaultRTRListAdapter extends PullToRequestListAdapter {
	private ScrollableRelativeLayout bodyView;
	private HeaderFooterProvider hfProvider;
	private NoDataViewItem emptyView;

	public DefaultRTRListAdapter(PullToRequestView view) {
		super(view);
		bodyView = new ScrollableRelativeLayout(view.getContext());
		hfProvider = new HeaderFooterProvider(view.getContext());

		ListView lv = getListView();
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bodyView.addView(lv, lp);

		emptyView = new NoDataViewItem(view.getContext());
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.topMargin = ResHelper.dipToPx(view.getContext(),120);
		bodyView.addView(emptyView, lp);
		emptyView.setVisibility(View.INVISIBLE);

		int resId = ResHelper.getBitmapRes(view.getContext(), "shopsdk_default_list_sep");
		getListView().setDivider(view.getContext().getResources().getDrawable(resId));
		getListView().setDividerHeight(1);
	}

	public void setEmptyRes(Context context,String resImg,String resTxt){
		emptyView.setEmptyImage(context,resImg);
		emptyView.setEmptyText(context,resTxt);
	}

	public Scrollable getBodyView() {
		return bodyView;
	}

	public void notifyDataSetChanged() {
		if (emptyView != null) {
			if (getCount() > 0) {
				emptyView.setVisibility(View.INVISIBLE);
			} else {
				emptyView.setVisibility(View.VISIBLE);
			}
		}
		super.notifyDataSetChanged();
	}

	public View getHeaderView() {
		return hfProvider.getHeaderView();
	}

	public void onPullDown(int percent) {
		hfProvider.onPullDown(percent);
	}

	public void onRefresh() {
		hfProvider.onRefresh();
		onRequest(true);
	}

	public View getFooterView() {
		return hfProvider.getFooterView();
	}

	public void onPullUp(int percent) {
		hfProvider.onPullUp(percent);
	}

	public void onRequestNext() {
		hfProvider.onRequestNext();
		onRequest(false);
	}

	protected abstract void onRequest(boolean firstPage);

	public void onReversed() {
		hfProvider.onReversed();
	}

	public class NoDataViewItem extends LinearLayout {
		private ImageView iv;
		private TextView tv;

		public NoDataViewItem(Context context) {
			super(context);
			initView(context);
		}

		public NoDataViewItem(Context context, AttributeSet attrs) {
			super(context, attrs);
			initView(context);
		}

		public NoDataViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			initView(context);
		}

		private void initView(Context context) {
			this.setOrientation(LinearLayout.VERTICAL);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			setLayoutParams(lp);

			iv = new ImageView(context);
			iv.setImageResource(ResHelper.getBitmapRes(context, "shopsdk_default_ptr_empty"));
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_HORIZONTAL;

			this.addView(iv, lp);

			tv = new TextView(context);
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(0xff999999);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			tv.setText(ResHelper.getStringRes(context, "shopsdk_default_no_data_currently"));
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_HORIZONTAL;
			lp.topMargin = ResHelper.dipToPx(context, 30);
			this.addView(tv, lp);
		}

		public void setEmptyImage(Context context, String resImg) {
			iv.setImageResource(ResHelper.getBitmapRes(context, resImg));
		}

		public void setEmptyText(Context context, String resTxt) {
			tv.setText(ResHelper.getStringRes(context, resTxt));
		}
	}

}
