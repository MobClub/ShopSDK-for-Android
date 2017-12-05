package com.mob.shop.gui.themes.defaultt.entity;

import java.io.Serializable;

/**
 * Created by yjin on 2017/9/8.
 */

public class GoodSelectedTypeItem implements Serializable {
	private String key;
	private String value;

	public GoodSelectedTypeItem() {
	}

	public GoodSelectedTypeItem(String key,String value){
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
