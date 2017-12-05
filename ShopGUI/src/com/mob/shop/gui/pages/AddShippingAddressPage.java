package com.mob.shop.gui.pages;


import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class AddShippingAddressPage extends Page<AddShippingAddressPage> {
	private ShippingAddr shippingAddr;

	public AddShippingAddressPage(Theme theme, ShippingAddr shippingAddr) {
		super(theme);
		this.shippingAddr = shippingAddr;
	}

	public ShippingAddr getShippingAddr() {
		return shippingAddr;
	}
}
