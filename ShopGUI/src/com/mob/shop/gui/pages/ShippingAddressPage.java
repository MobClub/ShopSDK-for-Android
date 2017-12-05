package com.mob.shop.gui.pages;


import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class ShippingAddressPage extends Page<ShippingAddressPage> {
    private long shippingId;

    public ShippingAddressPage(Theme theme, long shippingId) {
        super(theme);
        this.shippingId = shippingId;
    }

    public long getShippingId() {
        return shippingId;
    }
}
