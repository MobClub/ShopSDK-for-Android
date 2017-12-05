package com.mob.shop.gui.pages;


import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class PaySuccessPage extends Page<PaySuccessPage> {
	private long orderId;
	public PaySuccessPage(Theme theme, long orderId) {
		super(theme);
		this.orderId = orderId;
	}

	public long getOrderId() {
		return orderId;
	}
}
