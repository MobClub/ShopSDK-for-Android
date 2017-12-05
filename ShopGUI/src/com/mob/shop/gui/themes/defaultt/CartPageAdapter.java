package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.widget.LinearLayout;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.CartPage;
import com.mob.shop.gui.tabs.Tab;

/**
 * 购物车页
 */
public class CartPageAdapter extends DefaultThemePageAdapter<CartPage> {
	private LinearLayout llBody;
	private CartPage page;

	@Override
	public void onCreate(CartPage page, Activity activity) {
		super.onCreate(page, activity);
		this.page = page;
		activity.setContentView(R.layout.shopsdk_default_page_cart);
		initView(activity);
		setTab();
	}

	private void initView(Activity activity) {
		llBody = (LinearLayout) activity.findViewById(R.id.llBody);
	}

	private void setTab() {
		Tab tab = new CartTab(page, true);
		llBody.addView(tab.getTabView(getPage().getContext()), new LinearLayout.LayoutParams(LinearLayout.LayoutParams
				.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
	}
}
