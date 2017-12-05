package com.mob.shop.gui.pages;


import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

import java.util.List;

public class ShippingAddressManagePage extends Page<ShippingAddressManagePage> {
	private List<ShippingAddr> list;

	public ShippingAddressManagePage(Theme theme,List<ShippingAddr> list) {
		super(theme);
		this.list = list;
	}

	public List<ShippingAddr> getList() {
		return list;
	}
}
