package com.mob.shop.gui.themes.defaultt;

import android.content.Context;

import com.mob.shop.gui.base.DialogAdapter;
import com.mob.shop.gui.base.PageAdapter;
import com.mob.shop.gui.base.Theme;
import com.mob.tools.utils.ResHelper;

import java.util.Set;

public class DefaultTheme extends Theme {

	protected void initPageAdapters(Set<Class<? extends PageAdapter<?>>> adapters) {
		adapters.add(MainPageAdapter.class);
		adapters.add(OrderComfirmPageAdapter.class);
		adapters.add(ShippingAddressPageAdapter.class);
		adapters.add(AddShippingAddressPageAdapter.class);
		adapters.add(MyOrdersPageAdapter.class);
		adapters.add(OrderDetailPageAdapter.class);
		adapters.add(ApplyRefundPageAdapter.class);
		adapters.add(ShippingAddressManagePageAdapter.class);
		adapters.add(MineCouponsPageAdapter.class);
		adapters.add(SearchOrderPageAdapter.class);
		adapters.add(SearchOrderResultPageAdapter.class);
		adapters.add(CommodityDetailShowPageAdapter.class);
		adapters.add(ShowTransportMessagePageAdapter.class);
		adapters.add(AppraiseListPageAdapter.class);
		adapters.add(CartPageAdapter.class);
		adapters.add(SelectUseCouponsPageAdapter.class);
		adapters.add(ReceiveCouponPageAdapter.class);
		adapters.add(PaySuccessPageAdapter.class);
		adapters.add(PhotoCropPageAdapter.class);
		adapters.add(AppraisePageAdapter.class);
		adapters.add(RefundDetailPageAdapter.class);
		adapters.add(RefundListPageAdapter.class);
		adapters.add(CommodityDetailPageAdapter.class);
		adapters.add(ExpressStDReferPageAdapter.class);
		adapters.add(FillInExpressNumberPageAdapter.class);
		adapters.add(AboutMePageAdapter.class);
	}

	protected void initDialogAdapters(Set<Class<? extends DialogAdapter<?>>> adapters) {
		adapters.add(OKDialogAdapter.class);
		adapters.add(OKCancelDialogAdapter.class);
		adapters.add(ProgressDialogAdapter.class);
		adapters.add(ErrorDialogAdapter.class);
		adapters.add(ChoiceProductAttrDialogAdapter.class);
		adapters.add(SingleValuePickerAdapter.class);
		adapters.add(ImagePickerAdapter.class);
		adapters.add(PaySelectDialogAdapter.class);
	}

	public int getDialogStyle(Context context) {
		return ResHelper.getStyleRes(context, "shopsdk_default_DialogStyle");
	}

}
