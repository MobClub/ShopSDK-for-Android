package com.mob.shop.gui.pages.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.mob.shop.datatype.entity.Commodity;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.gui.base.Dialog;
import com.mob.shop.gui.base.Theme;

import java.util.HashMap;

public class ChoiceProductAttrDialog extends Dialog<ChoiceProductAttrDialog> {
	private Product product;
	private String selectStr;
	private Commodity commodity;
	private int buyCount;

	public ChoiceProductAttrDialog(Context context, Theme theme) {
		super(context, theme);
	}

	protected void applyParams(HashMap<String, Object> params) {
		super.applyParams(params);
		this.product = (Product) params.get("product");
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	public Product getProduct() {
		return product;
	}

	public void setSelectStr(String selectStr) {
		this.selectStr = selectStr;
	}

	public String getSelectStr() {
		return selectStr;
	}

	public static class Builder extends Dialog.Builder<ChoiceProductAttrDialog> {
		private Theme theme;
		private Throwable t;
		private OnDismissListener outListener;

		public Builder(Context context, Theme theme) {
			super(context, theme);
			this.theme = theme;
			setCanceledOnTouchOutside(true);
			setCancelable(true);
		}

		public void setThrowable(Throwable t) {
			this.t = t;
		}

		public Builder setOnDismissListener(OnDismissListener onDismissListener) {
			outListener = onDismissListener;
			return this;
		}

		public void setProduct(Product product){
			set("product",product);
		}

		public ChoiceProductAttrDialog create() throws Throwable {
			final ChoiceProductAttrDialog attrDialog = super.create();
			attrDialog.setOnDismissListener(new Dialog.OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					if (outListener != null) {
						outListener.onDismiss(attrDialog.getSelectStr(),attrDialog.getCommodity(),attrDialog.getBuyCount());
					}
				}
			});
			return attrDialog;
		}
		public interface OnDismissListener{
			void onDismiss(String selectStr,Commodity commodity,int count);
		}
	}
}
