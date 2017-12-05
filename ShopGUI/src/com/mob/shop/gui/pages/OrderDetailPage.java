package com.mob.shop.gui.pages;


import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class OrderDetailPage extends Page<OrderDetailPage> {
	private long orderId;
	public OrderDetailPage(Theme theme,long orderId) {
		super(theme);
		this.orderId = orderId;
	}

	public long getOrderId() {
		return orderId;
	}
}
