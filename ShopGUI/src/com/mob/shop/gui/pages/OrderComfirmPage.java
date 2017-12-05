package com.mob.shop.gui.pages;


import com.mob.shop.datatype.entity.PreOrder;
import com.mob.shop.gui.Callback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class OrderComfirmPage extends Page<OrderComfirmPage> {
	private PreOrder preOrder;

	public OrderComfirmPage(Theme theme, PreOrder preOrder,Callback callback) {
		super(theme);
		this.preOrder = preOrder;
		super.callback = callback;
	}

	public PreOrder getPreOrder() {
		return preOrder;
	}

	public void setPreOrder(PreOrder preOrder) {
		this.preOrder = preOrder;
	}
}
