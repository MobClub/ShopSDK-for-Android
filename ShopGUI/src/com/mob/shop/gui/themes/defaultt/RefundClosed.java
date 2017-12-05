package com.mob.shop.gui.themes.defaultt;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mob.shop.datatype.CloseType;
import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.gui.R;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.utils.ResHelper;

/**
 * Created by weishj on 2017/10/31.
 */

public class RefundClosed extends RefundBaseView {

	public RefundClosed(RefundDetailPageAdapter pageAdapter, RefundDescription refundDescription) {
		super(pageAdapter, refundDescription);
	}

	@Override
	protected View getView() {
		LayoutInflater inflater = LayoutInflater.from(pageAdapter.getPage().getContext());
		View view = inflater.inflate(R.layout.shopsdk_default_page_refund_detail_closed, null);
		return view;
	}

	@Override
	protected String getHeaderTitle() {
		if (refundDescription.getCloseType() == CloseType.SELLER_CLOSED) {
			return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
					pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_closed_seller_operated"));
		} else {
			return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
					pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_closed_buyer_operated"));
		}
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
		initData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		// Nothing to do
	}

	private void initData() {
		// Nothing to do
	}
}
