package com.mob.shop.gui.pages;


import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class CommodityDetailPage extends Page<CommodityDetailPage> {
	private String url;

	public CommodityDetailPage(Theme theme, String url) {
		super(theme);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
