package com.mob.shop.gui.pages;

import com.mob.shop.datatype.entity.Product;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

/**
 * Created by yjin on 2017/9/6.
 */

public class CommodityDetailShowPage extends Page<CommodityDetailShowPage> {
	private Product product;

    public CommodityDetailShowPage(Theme theme,Product product) {
        super(theme);
	    this.product = product;
    }

	public Product getProduct() {
		return product;
	}
}
