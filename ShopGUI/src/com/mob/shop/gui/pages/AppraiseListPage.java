package com.mob.shop.gui.pages;


import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;

public class AppraiseListPage extends Page<AppraiseListPage> {
	private long productId;
	private ImgUrl productImg;
	private String productName;

	public AppraiseListPage(Theme theme, long prodructId, ImgUrl productImg, String productName) {
		super(theme);
		this.productId = prodructId;
		this.productImg = productImg;
		this.productName = productName;
	}

	public long getProductId() {
		return productId;
	}

	public ImgUrl getProductImg() {
		return productImg;
	}

	public String getProductName() {
		return productName;
	}
}
