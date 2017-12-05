package com.mob.shop.gui.themes.defaultt.entity;

import java.util.List;

/**
 * Created by weishj on 2017/10/27.
 */

public class DrawerCondition {
	/** 最低价格（单位：分） */
	public int minPrice;
	/** 最高价格（单位：分） */
	public int maxPrice;
	/** 配送策略ID */
	public long transportStrategyId;
	/** 标签ID列表 */
	public List<Long> labelList;

	public DrawerCondition(int minPrice, int maxPrice, long transportStrategyId, List<Long> labelList) {
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.transportStrategyId = transportStrategyId;
		this.labelList = labelList;
	}

	public int getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public long getTransportStrategyId() {
		return transportStrategyId;
	}

	public void setTransportStrategyId(long transportStrategyId) {
		this.transportStrategyId = transportStrategyId;
	}

	public List<Long> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Long> labelList) {
		this.labelList = labelList;
	}
}
