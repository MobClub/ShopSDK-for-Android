package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.MainPage;
import com.mob.shop.gui.pages.OrderDetailPage;
import com.mob.shop.gui.pages.PaySuccessPage;
import com.mob.shop.gui.themes.defaultt.components.TitleView;

/**
 * 支付成功页
 */
public class PaySuccessPageAdapter extends DefaultThemePageAdapter<PaySuccessPage> implements View.OnClickListener {
	private PaySuccessPage page;
	private TitleView titleView;

	@Override
	public void onCreate(PaySuccessPage page, Activity activity) {
		super.onCreate(page, activity);
		this.page = page;
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_paysuccess, null);
		activity.setContentView(view);
		view.findViewById(R.id.tvHome).setOnClickListener(this);
		view.findViewById(R.id.tvOrder).setOnClickListener(this);
		titleView = (TitleView) view.findViewById(R.id.titleView);
		titleView.initTitleView(page,"shopsdk_default_pay_success","",null,true);
		titleView.setLeftClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.tvHome) {
			MainPage mainPage = new MainPage(getPage().getTheme());
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mainPage.show(getPage().getContext(), intent);
			finish();
		} else if (view.getId() == R.id.tvOrder) {
			OrderDetailPage orderDetailPage = new OrderDetailPage(getPage().getTheme(),page.getOrderId());
			orderDetailPage.show(getPage().getContext(), null);
			finish();
		}
	}
}
