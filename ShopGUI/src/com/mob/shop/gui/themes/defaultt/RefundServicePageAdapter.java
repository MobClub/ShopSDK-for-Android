package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.RefundServicePage;

/**
 * 退款售后页
 */

public class RefundServicePageAdapter extends DefaultThemePageAdapter<RefundServicePage> {

	private View view;
	private ListView refundServiceList;

	@Override
	public void onCreate(RefundServicePage page, Activity activity) {
		super.onCreate(page, activity);
		view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_refund_service,null);
		initView(view);
	}

	private void initView(View view){
		refundServiceList = (ListView)view.findViewById(R.id.listView);
	}


}
