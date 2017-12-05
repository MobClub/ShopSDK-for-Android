package com.mob.shop.gui.pay;

import android.content.Context;
import android.widget.Toast;



import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.PayResult;
import com.mob.shop.datatype.PayWay;
import com.mob.shop.datatype.builder.OrderPayBuilder;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.Theme;
import com.mob.shop.gui.pages.PaySuccessPage;

/**
 * Created by yjin on 2017/11/1.
 * <p>
 * 支付管理类
 */

public class PayManager {
	private static volatile PayManager instance = null;
	private Context context = null;
	private Theme page = null;
	OrderPayBuilder orderPayBuilder = null;

	private OperationCallback operationCallback;

	private void PayManager() {
	}

	public static PayManager getInstance() {
		if (instance == null) {
			synchronized (PayManager.class) {
				if (instance == null) {
					instance = new PayManager();
				}
			}
		}
		return instance;
	}

	public void pay(Context context, Theme page, OrderPayBuilder orderPayBuilder) {
		DialogUtils.showLoading(context);
		this.context = context;
		this.page = page;
		this.orderPayBuilder = orderPayBuilder;
		if (operationCallback != null) {
			DialogUtils.hidePayResult(context);
			ShopSDK.payOrder(orderPayBuilder, operationCallback);
			operationCallback = null;
		} else {
			ShopSDK.payOrder(orderPayBuilder, new MainOnPayListener());
		}

	}

	class MainOnPayListener extends OperationCallback {
		@Override
		public void onSuccess(Object data) {
			if(data != null){
				DialogUtils.hidePayResult(context);
				PayResult payResult = (PayResult) data;
				if (PayResult.SUCCESS == payResult) {
					PaySuccessPage paySuccessPage = new PaySuccessPage(page,orderPayBuilder.orderId);
					paySuccessPage.show(context, null);
				} else if (PayResult.CANCELED == payResult) {
					DialogUtils.hidePayResult(context);
					Toast.makeText(context, "支付取消", Toast.LENGTH_SHORT).show();
				} else {
					DialogUtils.hidePayResult(context);
					DialogUtils.showPayResult(context, false);
				}
			}
		}

		@Override
		public void onFailed(Throwable t) {

			DialogUtils.showPayResult(context, false);
		}
	}

	public void setOperationCallback(OperationCallback<com.mob.shop.datatype.PayResult> callback) {
		this.operationCallback = callback;
	}
}
