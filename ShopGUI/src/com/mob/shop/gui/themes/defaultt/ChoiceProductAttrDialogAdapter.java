package com.mob.shop.gui.themes.defaultt;


import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.biz.api.exception.ShopError;
import com.mob.shop.biz.api.exception.ShopException;
import com.mob.shop.datatype.entity.Commodity;
import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.datatype.entity.ProductProperty;
import com.mob.shop.datatype.entity.ProductPropertyValue;
import com.mob.shop.datatype.entity.SelectableProperty;
import com.mob.shop.datatype.entity.SelectedProperty;
import com.mob.shop.gui.pages.dialog.ChoiceProductAttrDialog;
import com.mob.shop.gui.themes.defaultt.components.GoodDetailShowSelected;
import com.mob.shop.gui.themes.defaultt.entity.GoodSelectedTypeItem;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择商品型号Dialog
 */
public class ChoiceProductAttrDialogAdapter extends ScrollUpAndDownDialogAdapter<ChoiceProductAttrDialog> {
	private ChoiceProductAttrDialog dialog;
	private GoodDetailShowSelected goodDetailShowSelected;
	private Commodity commodity;

	@Override
	public void init(ChoiceProductAttrDialog dialog) {
		this.dialog = dialog;
		super.init(dialog);
	}

	@Override
	protected void initBodyView(LinearLayout llBody) {
		final Context context = dialog.getContext();
		goodDetailShowSelected = new GoodDetailShowSelected(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
				.LayoutParams.WRAP_CONTENT);
		goodDetailShowSelected.setLayoutParams(lp);

		llBody.addView(goodDetailShowSelected);
		llBody.setBackgroundColor(Color.parseColor("#00ffffff"));

		final Product product = dialog.getProduct();
		goodDetailShowSelected.setSalesCounts(1);
		goodDetailShowSelected.initData(product.getMinPrice(), product.getMaxPrice(), product.getProductImgUrls() !=
				null ? product.getProductImgUrls().get(0) : new ImgUrl(), product.getCommodityList() != null ? product
				.getCommodityList().get(0).getUsableStock() : 0);

		if (product.getProductPropertyList() == null || product.getProductPropertyList().size() == 0) {
			commodity = (product.getCommodityList() == null || product.getCommodityList().size() == 0) ? null :
					product.getCommodityList().get(0);
			goodDetailShowSelected.setData(commodity);
			goodDetailShowSelected.setSelectedStr("");
		}

		ArrayList<ProductProperty> productProperties = product.getProductPropertyList();
		if (productProperties != null) {
			goodDetailShowSelected.setCollspanBarTitleCount(productProperties.size());
			for (int i = 0; i < productProperties.size(); i++) {
				ArrayList<ProductPropertyValue> propertyValues = productProperties.get(i).getPropertyValues();
				if (propertyValues != null) {
					String key = productProperties.get(i).getPropertyKey();
					ArrayList<GoodSelectedTypeItem> list = new ArrayList<GoodSelectedTypeItem>();
					for (ProductPropertyValue propertyValue : propertyValues) {
						GoodSelectedTypeItem goodSelectedTypeItem = new GoodSelectedTypeItem(key, propertyValue
								.getPropertyValue());
						list.add(goodSelectedTypeItem);
					}
					goodDetailShowSelected.addSelectedType(i, key, list, context);
				}
			}
		}

		goodDetailShowSelected.setOnSelectedChangeListener(new GoodDetailShowSelected.OnSelectedChangeListener() {
			@Override
			public void onSelectedChange(String type, GoodSelectedTypeItem[] list) {
				List<SelectedProperty> selectedProperties = new ArrayList<SelectedProperty>();
				for (int i = 0; i < list.length; i++) {
					GoodSelectedTypeItem selectedTypeItem = list[i];
					if (selectedTypeItem != null) {
						SelectedProperty selectedProperty = new SelectedProperty(selectedTypeItem.getKey(),
								selectedTypeItem.getValue());
						selectedProperties.add(selectedProperty);
					}
				}
				getSelectableProperties(product, selectedProperties);
			}

			@Override
			public void submit() {
				if (commodity == null) {
					Toast.makeText(context, context.getString(ResHelper.getStringRes(context,
							"shopsdk_default_select_product_model")), Toast.LENGTH_SHORT).show();
					return;
				}
				dialog.dismiss();
				dialog.setBuyCount(goodDetailShowSelected.getBuyCount());
				dialog.setCommodity(commodity);
				dialog.setSelectStr(goodDetailShowSelected.getSelectedStr());
			}

			@Override
			public void close() {
				dialog.dismiss();
				dialog.setBuyCount(-1);
			}
		});

	}

	private void getSelectableProperties(final Product product, final List<SelectedProperty> list) {
		ShopSDK.getSelectableProperties(product, list, new OperationCallback<List<? extends ProductProperty>>() {
			@Override
			public void onSuccess(List<? extends ProductProperty> data) {
				super.onSuccess(data);
				commodity = null;
				if (data != null) {
					for (ProductProperty productProperty : data) {
						if (productProperty instanceof SelectableProperty) {
							List<GoodSelectedTypeItem> typeItems = new ArrayList<GoodSelectedTypeItem>();
							HashMap<String, String> map = new HashMap<String, String>();
							for (ProductPropertyValue propertyValue : productProperty.getPropertyValues()) {
								GoodSelectedTypeItem typeItem = new GoodSelectedTypeItem(propertyValue
										.getPropertyValue(), propertyValue.getPropertyValue());
								typeItems.add(typeItem);
								map.put(propertyValue.getPropertyValue(), propertyValue.getPropertyValue());
							}
							goodDetailShowSelected.changeSelectedType(productProperty.getPropertyKey(), map);
						}
					}
				}

			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);
				if (t instanceof ShopException) {
					int code = ((ShopException) t).getCode();
					if (code == ShopError.C_ILLEGAL_PARAMS.getCode()) {
						specifyCommodityBySelectedProperties(product, list);
					}
				}
			}
		});
	}

	private void specifyCommodityBySelectedProperties(Product product, List<SelectedProperty> selectedPropertyList) {
		ShopSDK.specifyCommodityBySelectedProperties(product, selectedPropertyList, new OperationCallback<Commodity>
				() {
			@Override
			public void onSuccess(Commodity data) {
				super.onSuccess(data);
				goodDetailShowSelected.setData(data);
				commodity = data;
			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);
			}
		});
	}
}
