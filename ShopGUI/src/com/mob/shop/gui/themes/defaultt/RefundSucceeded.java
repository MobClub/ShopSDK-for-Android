package com.mob.shop.gui.themes.defaultt;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.TransportMsgPage;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.utils.ResHelper;

/**
 * Created by weishj on 2017/10/31.
 */

public class RefundSucceeded extends RefundBaseView {
	private TextView refundFeeTv;

	public RefundSucceeded(RefundDetailPageAdapter pageAdapter, RefundDescription refundDescription) {
		super(pageAdapter, refundDescription);
	}

	@Override
	protected View getView() {
		LayoutInflater inflater = LayoutInflater.from(pageAdapter.getPage().getContext());
		View view = inflater.inflate(R.layout.shopsdk_default_page_refund_detail_succeeded, null);
		return view;
	}

	@Override
	protected String getHeaderTitle() {
		return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
				pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_succeeded"));
	}

	/**
	 * Get header expiration description, return null or "" if do not want header expiration LinearLayout to be displayed
	 *
	 * @return
	 */
	@Override
	protected String getHeaderExpirationDesc() {
		return null;
	}

	/**
	 * Any RefundBaseView that located at the top level will display footer LinearLayout, return true if want the footer
	 * to be not displayed
	 *
	 * @return
	 */
	@Override
	protected boolean forceDisableFooter() {
		return true;
	}

	@Override
	protected void initView() {
		super.initView();
		this.refundFeeTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_succeeded_fee_tv);
		initData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		// Nothing to do
	}

	private void initData() {
		this.refundFeeTv.setText(MoneyConverter.conversionMoneyStr(refundDescription.getRefundFee()));
	}
}
