package com.mob.shop.gui.pages;


import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class SearchOrderResultPage extends Page<SearchOrderResultPage> {
	private String search;
	public SearchOrderResultPage(Theme theme,String search) {
		super(theme);
		this.search = search;
	}

	public String getSearch() {
		return search;
	}
}
