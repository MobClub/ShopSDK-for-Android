package com.mob.shop.gui.themes.defaultt.components.imple;

import com.mob.shop.gui.themes.defaultt.components.NormalTextView;
import com.mob.shop.gui.themes.defaultt.components.PricesTextView;

/**
 * Created by yjin on 2017/10/13.
 */

public interface SelectManagerImpl {

	void changePrices(NormalTextView prices,NormalTextView newProduct);

	void changeNew(NormalTextView prices, PricesTextView textView);

	void changeSales(NormalTextView newProduct, PricesTextView textView);

}
