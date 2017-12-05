package com.mob.shop.gui.pages;


import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class RefundListPage extends Page<RefundListPage> {
	private String keyword ="";
	public RefundListPage(Theme theme,String keyword) {
		super(theme);
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}
}
