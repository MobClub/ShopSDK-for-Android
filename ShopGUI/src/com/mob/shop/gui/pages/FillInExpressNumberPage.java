package com.mob.shop.gui.pages;

import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

/**
 * Created by yjin on 2017/11/2.
 * 填写订单号
 */

public class FillInExpressNumberPage extends Page<FillInExpressNumberPage> {
	private long orderCommodityId;

	public FillInExpressNumberPage(Theme theme,long orderCommodityId){
		super(theme);
		this.orderCommodityId = orderCommodityId;
	}

	public long getOrderCommodityId(){
		return orderCommodityId;
	}
	public FillInExpressNumberPage(Theme theme) {
		super(theme);
	}

}
