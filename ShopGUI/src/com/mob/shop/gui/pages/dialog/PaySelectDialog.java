package com.mob.shop.gui.pages.dialog;

import android.content.Context;

import com.mob.shop.datatype.builder.OrderPayBuilder;
import com.mob.shop.gui.base.Dialog;
import com.mob.shop.gui.base.Theme;


import java.util.HashMap;


/**
 * Created by yjin on 2017/11/1.
 */

public class PaySelectDialog extends Dialog<PaySelectDialog> {

	private OrderPayBuilder payEntity;//支付所需的参数；
	private PayCancelError payCancelError = null;
	public PaySelectDialog(Context context, Theme theme) {
		super(context, theme);
	}

	public Theme getParentTheme(){
		return getTheme();
	}

	@Override
	protected void applyParams(HashMap<String, Object> params) {
		super.applyParams(params);
		this.payEntity = (OrderPayBuilder) params.get("payEntity");
	}

	public OrderPayBuilder getPayEntity() {
		return payEntity;
	}

	public void setPayEntity(OrderPayBuilder payEntity) {
		this.payEntity = payEntity;
	}

	public static class Builder extends Dialog.Builder<PaySelectDialog>{

		private Theme theme;

		public Builder(Context context, Theme theme) {
			super(context, theme);
			this.theme = theme;
		}

		public void setPayEntity(OrderPayBuilder payEntity){
			set("payEntity",payEntity);
		}

		public PaySelectDialog create() throws Throwable{
			final PaySelectDialog dialog = super.create();
			return dialog;
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();

	}

	public void onCancel(){
		if(payCancelError != null){
			payCancelError.onCancel();
			//payCancelError.onError();
		}
	}

	public void setPayCancelError(PayCancelError payCancelError){
		this.payCancelError = payCancelError;
	}

	public interface  PayCancelError{
		void onCancel();
		void onError();
	}
}
