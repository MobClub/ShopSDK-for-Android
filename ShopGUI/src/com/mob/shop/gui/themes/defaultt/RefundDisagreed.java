package com.mob.shop.gui.themes.defaultt;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.ApplyRefundPage;
import com.mob.tools.utils.ResHelper;

/**
 * Created by weishj on 2017/10/31.
 */

public class RefundDisagreed extends RefundBaseView {
	private TextView disagreeReasonTv;

	public RefundDisagreed(RefundDetailPageAdapter pageAdapter, RefundDescription refundDescription) {
		super(pageAdapter, refundDescription);
	}

	@Override
	protected View getView() {
		LayoutInflater inflater = LayoutInflater.from(pageAdapter.getPage().getContext());
		View view = inflater.inflate(R.layout.shopsdk_default_page_refund_detail_disagreed, null);
		return view;
	}

	@Override
	protected String getHeaderTitle() {
		return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
				pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_disagreed"));
	}

	/**
	 * Get header expiration description, return null or "" if do not want header expiration LinearLayout to be displayed
	 *
	 * @return
	 */
	@Override
	protected String getHeaderExpirationDesc() {
		return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
				pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_expiration_desc_buyer_operating"));
	}

	/**
	 * Any RefundBaseView that located at the top level will display footer LinearLayout, return true if want the footer
	 * to be not displayed
	 *
	 * @return
	 */
	@Override
	protected boolean forceDisableFooter() {
		return false;
	}

	@Override
	protected void initView() {
		super.initView();
		this.disagreeReasonTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_disagree_reason_tv);
		if (expiration > 0) {
			reapplyTv.setVisibility(VISIBLE);
			cancelTv.setVisibility(VISIBLE);
		}
		initData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.shopsdk_refund_detail_reapply_tv) {
			gotoApplyRefundPage();
		}
	}

	private void initData() {
		this.disagreeReasonTv.setText(refundDescription.getRejectReason());
	}

	private void gotoApplyRefundPage() {
		ApplyRefundPage applyRefundPage = new ApplyRefundPage(pageAdapter.getPage().getTheme(), orderCommodityId);
		applyRefundPage.show(getContext(), null);
	}
}
