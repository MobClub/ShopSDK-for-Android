package com.mob.shop.demo.util;

import android.content.Context;
import android.util.Log;

import com.mob.shop.datatype.entity.Order;
import com.mob.shop.demo.net.AbstractHttp;
import com.mob.shop.demo.net.HttpCallBack;
import com.mob.shop.demo.net.NetworkHelper;
import com.mob.shop.gui.pay.customizedpay.CustomizedPayListener;
import com.mob.shop.gui.pay.customizedpay.PayResult;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weishj on 2017/12/20.
 */

public class PayHelper {
	private static final String TAG = PayHelper.class.getSimpleName();
	public static final String PAY_URL = "http://demopay.shop.mob.com/pay/wx/unifiedorder/";
	private static Context context;
	private final IWXAPI iwxapi;
	private NetworkHelper networkHelper;
	private Order order;
	private CustomizedPayListener customizedPayListener;

	private PayHelper() {
		iwxapi = WXAPIFactory.createWXAPI(context, Constant.WXAPPID);
		networkHelper = new NetworkHelper();
	}

	public static final PayHelper getInstance(Context context) {
		PayHelper.context = context;
		return SingletonHolder.INSTANCE;
	}

	public void pay(Order order, CustomizedPayListener listener) {
		this.customizedPayListener = listener;
		if (order == null) {
			Log.e(TAG, "order is null");
			notifyPayResult(PayResult.FAIL, null);
			return;
		}
		this.order = order;
		if (this.order.isFreeOfCharge()) {
			Log.d(TAG, "This order is free of charge, notify PayResult.SUCCESS directly");
			notifyPayResult(PayResult.SUCCESS, null);
		} else {
			if (isPaySupported()) {
				if (networkHelper == null) {
					networkHelper = new NetworkHelper();
				}
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("body", Utils.getAppName(context) + "-商品购买");
				params.put("outTradeNo", String.valueOf(this.order.getOrderId()));
				params.put("totalFee", this.order.getPaidMoney());
				params.put("spbillCreateIp", Utils.getIPAddress());
				params.put("tradeType", "APP");
				networkHelper.asyncConnect(PAY_URL, params, AbstractHttp.HttpMethod.POST, new HttpCallBack<String>() {
					@Override
					public void onStart(String url) {}

					@Override
					public void onLoading(long progress, long count) {}

					@Override
					public void onSuccess(String s) {
						Log.d(TAG, "Obtain WeChat pay params onSuccess. response: " + s);
						try {
							JSONObject json = new JSONObject(s);
							if ("true".equals(json.optString("success"))) {
								PayReq req = new PayReq();
								JSONObject data = json.optJSONObject("data");
								if (data != null) {
									req.partnerId = data.optString("partnerid");
									req.prepayId = data.optString("prepayid");
									req.nonceStr = data.optString("noncestr");
									req.sign = data.optString("sign");
									req.packageValue = data.optString("package");
									req.timeStamp = data.optString("timestamp");
								}
								req.appId = Constant.WXAPPID;
								Log.d(TAG, "Send pay request to wechat");
								iwxapi.sendReq(req);
							} else if ("false".equals(json.optString("success"))) {
								String errMsg = json.optString("errMsg");
								Log.d(TAG, errMsg == null ? "" : errMsg);
								notifyPayResult(PayResult.FAIL, null);
							} else {
								Log.e(TAG, "Unknown response data");
								notifyPayResult(PayResult.FAIL, null);
							}
						} catch (JSONException e) {
							Log.e(TAG, "Server response data format error", e);
							notifyPayResult(PayResult.FAIL, null);
						}
					}

					@Override
					public void onFailure(int responseCode, Throwable e) {
						Log.e(TAG, "Obtain WeChat pay params onFailure. responseCode: " + responseCode, e);
						notifyPayResult(PayResult.FAIL, null);
					}

					@Override
					public void onCancel() {}
				});
			} else {
				Log.e(TAG, "Pay is not supported by the current WeChat version");
				notifyPayResult(PayResult.FAIL, null);
			}
		}
	}

	public void notifyPayResult(PayResult payResult, String payTicket) {
		Log.d(TAG, "notifyPayResult. payResult= " + payResult + ", payTicket= " + payTicket);
		if (customizedPayListener != null) {
			customizedPayListener.onPayEnd(order.getOrderId(), payResult, payTicket);
		}
	}

	private boolean isPaySupported() {
		return iwxapi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
	}

	private static class SingletonHolder {
		private static final PayHelper INSTANCE = new PayHelper();
	}
}
