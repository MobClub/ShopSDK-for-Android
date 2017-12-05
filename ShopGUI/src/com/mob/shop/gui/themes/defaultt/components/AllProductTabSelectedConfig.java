package com.mob.shop.gui.themes.defaultt.components;

import com.mob.shop.gui.themes.defaultt.components.imple.SelectManagerImpl;

/**
 * 按钮重置逻辑
 */

public class AllProductTabSelectedConfig implements SelectManagerImpl {

	private NormalTextView salCounts;
	private NormalTextView newPro;

	public AllProductTabSelectedConfig(){

	}

	public AllProductTabSelectedConfig(NormalTextView salCounts,NormalTextView newPro){
		this.salCounts = salCounts;
		this.newPro = newPro;
	}

	@Override
	public void changePrices(NormalTextView prices, NormalTextView newProduct) {
		prices.reset();
		newProduct.reset();
	}

	@Override
	public void changeNew(NormalTextView prices, PricesTextView textView) {
		prices.reset();
		textView.reset();
	}

	@Override
	public void changeSales(NormalTextView newProduct, PricesTextView textView) {
		newProduct.reset();
		textView.reset();
	}
	public void selectPrices(){
		salCounts.reset();
		newPro.reset();
	}
}
