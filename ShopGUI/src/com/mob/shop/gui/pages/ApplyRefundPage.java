package com.mob.shop.gui.pages;


import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class ApplyRefundPage extends Page<ApplyRefundPage> {
	private long orderCommodityId;

	public ApplyRefundPage(Theme theme, long orderCommodityId) {
		super(theme);
		this.orderCommodityId = orderCommodityId;
	}

	public long getOrderCommodityId() {
		return orderCommodityId;
	}
}
