package com.mob.shop.gui.pages;

import com.mob.shop.datatype.entity.Order;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

/**
 * 评价页面
 */

public class AppraisePage extends Page<AppraisePage> {
	private Order order;
	public AppraisePage(Theme theme,Order order) {
		super(theme);
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}
}
