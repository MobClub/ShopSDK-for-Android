package com.mob.shop.gui.pages;

import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;
import com.mob.shop.gui.themes.defaultt.entity.ExpressCompanyBundle;

/**
 * Created by yjin on 2017/11/2.
 * 承运方的页面
 */

public class ExpressStDReferPage extends Page<ExpressStDReferPage> {
	private ExpressCompanyBundle expressCompanyBundle = null;
	public ExpressStDReferPage(Theme theme) {
		super(theme);
	}

	public ExpressCompanyBundle getExpressCompanyBundle() {
		return expressCompanyBundle;
	}

	public void setExpressCompanyBundle(ExpressCompanyBundle expressCompanyBundle) {
		this.expressCompanyBundle = expressCompanyBundle;
	}
}
