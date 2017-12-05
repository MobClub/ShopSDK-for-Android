package com.mob.shop.gui.themes.defaultt.entity;

import java.util.List;

/**
 * Created by yjin on 2017/10/12.
 * 侧栏选择的实体类
 */

public class AllGoodSelectParams {
	private Integer minPrices;//最低价格
	private Integer maxPrices;//最高价格
	private Long transportStrategyId;//策略ID
	private List<Long> lableList;//标签集合

	public Integer getMinPrices() {
		return minPrices;
	}

	public void setMinPrices(Integer minPrices) {
		this.minPrices = minPrices;
	}
}
