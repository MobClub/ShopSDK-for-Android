package com.mob.shop.gui.pages;


import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class MyOrdersPage extends Page<MyOrdersPage> {
	private int status;
	public MyOrdersPage(Theme theme,int status) {
		super(theme);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
