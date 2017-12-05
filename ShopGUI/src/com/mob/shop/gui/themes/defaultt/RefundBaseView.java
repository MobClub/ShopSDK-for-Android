package com.mob.shop.gui.themes.defaultt;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.dialog.OKCancelDialog;
import com.mob.shop.gui.utils.Utils;
import com.mob.tools.utils.ResHelper;

/**
 * Created by weishj on 2017/10/31.
 */

public abstract class RefundBaseView extends LinearLayout implements View.OnClickListener {
	protected RefundDetailPageAdapter pageAdapter;
	protected TextView headerTitle;
	protected TextView headerTime;
	protected LinearLayout headerExpirationLl;
	protected TextView headerExpiration;
	protected TextView headerExpirationDesc;
	protected TextView modifyRefundTv;
	protected TextView fillInExpressTv;
	protected TextView modifyExpressTv;
	protected TextView queryExpressTv;
	protected TextView reapplyTv;
	protected TextView cancelTv;
	protected RefundDescription refundDescription;
	protected int expiration;
	protected long orderCommodityId;
	protected View view;
	protected OnRequestRefundDetailListener onRequestRefundDetailListener;

	public RefundBaseView(RefundDetailPageAdapter pageAdapter, RefundDescription refundDescription) {
		super(pageAdapter.getPage().getContext());
		this.pageAdapter = pageAdapter;
		this.refundDescription = refundDescription;
		view = getView();
	}

	protected abstract View getView();
	protected abstract String getHeaderTitle();

	/**
	 * Get header expiration description, return null or "" if do not want header expiration LinearLayout to be displayed
	 *
	 * @return
	 */
	protected abstract String getHeaderExpirationDesc();

	/**
	 * Any RefundBaseView that located at the top level will display footer LinearLayout, return true if want the footer
	 * to be not displayed
	 * @return
	 */
	protected abstract boolean forceDisableFooter();

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		addView(view);
		initView();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.shopsdk_refund_detail_cancel_tv) {
			confirmCancelRefund();
		}
	}

	public void setOnRequestRefundDetailListener(OnRequestRefundDetailListener l) {
		this.onRequestRefundDetailListener = l;
	}

	protected void initView() {
		// Content header components
		headerTitle = (TextView)view.findViewById(R.id.shopsdk_refund_detail_content_header_title);
		headerTime = (TextView)view.findViewById(R.id.shopsdk_refund_detail_content_header_time);
		headerExpirationLl = (LinearLayout)view.findViewById(R.id.shopsdk_refund_detail_content_header_expiration_ll);
		headerExpiration = (TextView)view.findViewById(R.id.shopsdk_refund_detail_content_header_expiration);
		headerExpirationDesc = (TextView)view.findViewById(R.id.shopsdk_refund_detail_content_header_expiration_desc);
		// Content footer components
		modifyRefundTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_modify_refund_tv);
		modifyRefundTv.setOnClickListener(this);
		fillInExpressTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_fill_in_express_tv);
		fillInExpressTv.setOnClickListener(this);
		modifyExpressTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_modify_express_tv);
		modifyExpressTv.setOnClickListener(this);
		queryExpressTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_query_express_tv);
		queryExpressTv.setOnClickListener(this);
		reapplyTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_reapply_tv);
		reapplyTv.setOnClickListener(this);
		cancelTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_cancel_tv);
		cancelTv.setOnClickListener(this);

		initData();
	}

	private void initData() {
		headerTitle.setText(getHeaderTitle());
		headerTime.setText(Utils.long2Time(refundDescription.getCreateAt(), Utils.FORMAT_YYYY_MM_DD_HH_MM_SS));
		if (this.expiration > 0) {
			headerExpirationLl.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(getHeaderExpirationDesc())) {
				((LinearLayout)headerExpiration.getParent()).setVisibility(GONE);
			} else {
				((LinearLayout)headerExpiration.getParent()).setVisibility(VISIBLE);
				headerExpiration.setText(Utils.getCountDownTime(expiration, Utils.Accuracy.MINUTE));
				headerExpirationDesc.setText(getHeaderExpirationDesc());
			}
			if (!forceDisableFooter()) {
				// Show Content footer LinearLayout
				((LinearLayout)modifyRefundTv.getParent().getParent()).setVisibility(View.VISIBLE);
			}
		}
	}

	private void confirmCancelRefund() {
		OKCancelDialog.Builder builder = new OKCancelDialog.Builder(getContext(), pageAdapter.getPage().getTheme());
		builder.setMessage(getContext().getResources().getString(ResHelper.getStringRes(getContext(),
				"shopsdk_default_refund_detail_msg_confirm_cancel")));
		builder.setButtonCancel(getContext().getResources().getString(ResHelper.getStringRes(getContext(),
				"shopsdk_default_refund_detail_confirm_btn_cancel")));
		builder.setButtonOK(getContext().getResources().getString(ResHelper.getStringRes(getContext(),
				"shopsdk_default_refund_detail_confirm_btn_confirm")));
		builder.setOnClickListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE: {
						cancelRefund();
						break;
					}
					case DialogInterface.BUTTON_NEGATIVE: {
						// Nothing to do
					}
				}
				dialog.dismiss();
			}
		});
		try {
			OKCancelDialog dialog = builder.create();
			dialog.show();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private void cancelRefund() {
		ShopSDK.cancelRefund(orderCommodityId, new OperationCallback<Void>() {
			@Override
			public void onSuccess(Void data) {
				super.onSuccess(data);
				if (onRequestRefundDetailListener != null) {
					onRequestRefundDetailListener.onRequestRefundDetail();
				}
			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);
				pageAdapter.getPage().toastNetworkError();
			}
		});
	}

	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

	public void setOrderCommodityId(long orderCommodityId) {
		this.orderCommodityId = orderCommodityId;
	}

	public interface OnRequestRefundDetailListener {
		void onRequestRefundDetail();
	}

}
