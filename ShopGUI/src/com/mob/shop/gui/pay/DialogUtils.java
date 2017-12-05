package com.mob.shop.gui.pay;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.mob.shop.gui.R;


public class DialogUtils {

	private static Dialog loadingDialog;
	private static Dialog payResultDialog;

	/**
	 * 显示支付结果dialog
	 */
	public static boolean showPayResult(Context context, boolean success) {
		hideAll(context);
		payResultDialog = new Dialog(context, R.style.Dialog);
		payResultDialog.setContentView(R.layout.shopsdk_default_main_payresult_dialog);
		payResultDialog.getWindow().setBackgroundDrawable(null);

		TextView tv = (TextView) payResultDialog.findViewById(R.id.tv_payresult);
			tv.setText(R.string.dialog_pay_fail);
			Drawable d = context.getResources().getDrawable(R.drawable.main_payresult_error);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			tv.setCompoundDrawables(null, d, null, null);
		payResultDialog.setCanceledOnTouchOutside(true);
		payResultDialog.setCancelable(true);
		payResultDialog.show();
		return true;
	}

	/**
	 * 隐藏支付结果dialog
	 */
	public static boolean hidePayResult(Context context) {
		hideAll(context);
		return true;
	}


	/**
	 * 显示loading
	 */
	public static boolean showLoading(Context context) {
		hideAll(context);
		loadingDialog = new Dialog(context, R.style.Dialog);
		loadingDialog.setContentView(R.layout.shopsdk_default_loading_dialog);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(false);
		loadingDialog.show();
		return true;
	}

	/**
	 * 隐藏loading
	 */
	public static boolean hideLoading(Context context) {
		hideAll(context);
		return true;
	}

	private static boolean hideAll(Context context) {
		if (null != loadingDialog && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
		if (null != payResultDialog && payResultDialog.isShowing()) {
			payResultDialog.dismiss();
		}
		return true;
	}

}
