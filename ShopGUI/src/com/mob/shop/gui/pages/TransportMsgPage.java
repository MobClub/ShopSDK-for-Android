package com.mob.shop.gui.pages;

import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;


public class TransportMsgPage extends Page<TransportMsgPage> {
	private long transportId;

	public TransportMsgPage(Theme theme, long transportId) {
		super(theme);
		this.transportId = transportId;
	}

	public long getTransportId() {
		return transportId;
	}
}
