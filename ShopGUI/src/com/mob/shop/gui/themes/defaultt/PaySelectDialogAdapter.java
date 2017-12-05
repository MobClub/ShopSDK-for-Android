package com.mob.shop.gui.themes.defaultt;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mob.shop.datatype.builder.OrderPayBuilder;
import com.mob.shop.gui.pages.dialog.PaySelectDialog;
import com.mob.shop.gui.themes.defaultt.components.PaySelectedView;
import com.mob.shop.gui.themes.defaultt.entity.PayEntity;

/**
 * Created by yjin on 2017/11/1.
 */

public class PaySelectDialogAdapter extends ScrollUpAndDownDialogAdapter<PaySelectDialog> {

	private PaySelectDialog paySelectDialog;
	private PaySelectedView paySelectedView;

	@Override
	public void init(PaySelectDialog dialog) {
		this.paySelectDialog = dialog;
		super.init(dialog);
	}

	@Override
	protected void initBodyView(LinearLayout llBody) {
		super.initBodyView(llBody);
		final Context context = paySelectDialog.getContext();
		paySelectedView = new PaySelectedView(context);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
				.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		paySelectedView.setLayoutParams(lp);
		llBody.addView(paySelectedView);
		llBody.setBackgroundColor(Color.parseColor("#ffffff"));
		OrderPayBuilder payEntity = paySelectDialog.getPayEntity();
		if (payEntity != null) {
			paySelectedView.setData(context,paySelectDialog.getParentTheme(),payEntity);
		}
		paySelectedView.setPayCancel(new PaySelectedView.PayCancelImpl() {
			@Override
			public void payCancel() {
				paySelectDialog.dismiss();
				paySelectDialog.onCancel();
			}

			@Override
			public void onPay() {
				paySelectDialog.dismiss();
			}
		});
	}
}
