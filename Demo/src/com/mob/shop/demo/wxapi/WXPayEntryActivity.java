package com.mob.shop.demo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mob.shop.demo.R;
import com.mob.shop.demo.util.Constant;
import com.mob.shop.demo.util.PayHelper;
import com.mob.shop.gui.pay.customizedpay.PayResult;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = WXPayEntryActivity.class.getSimpleName();
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_wxpayentry);

    	api = WXAPIFactory.createWXAPI(getApplicationContext(), Constant.WXAPPID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(TAG, "onNewIntent");
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			String payTicket = null;
			PayResult payResult;
			int errorCode = resp.errCode;
			String errMsg = resp.errStr;
			String desc;
			// Success
			if (errorCode == 0) {
				payResult = PayResult.SUCCESS;
				desc = errorCode + "-success";
			// Cancel
			} else if (errorCode == -2) {
				payResult = PayResult.CANCEL;
				desc = errorCode + "-cancel";
			// Failed
			} else if (errorCode == -1) {
				payResult = PayResult.FAIL;
				desc = errorCode + "-fail";
			} else {
				Log.w(TAG, "Unknown WeChat pay errorCode. errorCode= " + errorCode);
				payResult = PayResult.FAIL;
				desc = errorCode + "-unknown";
			}

			try {
				JSONObject json = new JSONObject();
				json.put("errCode", desc);
				json.put("errStr", errMsg);
				payTicket = json.toString();
			} catch (JSONException e) {
				Log.w(TAG, "Generate PayTicket error.", e);
			}
			PayHelper.getInstance(getApplicationContext()).notifyPayResult(payResult, payTicket);
			finish();
		}
	}
}