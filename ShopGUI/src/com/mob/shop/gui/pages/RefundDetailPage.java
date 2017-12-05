package com.mob.shop.gui.pages;

import com.mob.shop.datatype.entity.Refund;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

/**
 * Created by yjin on 2017/9/13.
 */

public class RefundDetailPage extends Page<RefundDetailPage> {
	private long orderCommodityId;

	public RefundDetailPage(Theme theme, long orderCommodityId) {
		super(theme);
		this.orderCommodityId = orderCommodityId;
	}

	public long getOrderCommodityId() {
		return orderCommodityId;
	}
}
