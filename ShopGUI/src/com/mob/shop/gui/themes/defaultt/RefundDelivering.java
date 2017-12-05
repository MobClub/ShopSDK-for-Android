package com.mob.shop.gui.themes.defaultt;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.FillInExpressNumberPage;
import com.mob.shop.gui.pages.TransportMsgPage;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;

/**
 * Created by weishj on 2017/10/31.
 */

public class RefundDelivering extends RefundBaseView {
	private TextView expressNoTv;
	private TextView expressCompanyTv;

	public RefundDelivering(RefundDetailPageAdapter pageAdapter, RefundDescription refundDescription) {
		super(pageAdapter, refundDescription);
	}

	@Override
	protected View getView() {
		LayoutInflater inflater = LayoutInflater.from(pageAdapter.getPage().getContext());
		View view = inflater.inflate(R.layout.shopsdk_default_page_refund_detail_delivering, null);
		return view;
	}

	@Override
	protected String getHeaderTitle() {
		return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
				pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_delivering"));
	}

	/**
	 * Get header expiration description, return null or "" if do not want header expiration LinearLayout to be displayed
	 *
	 * @return
	 */
	@Override
	protected String getHeaderExpirationDesc() {
		return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
				pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_expiration_desc_seller_operating"));
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
		this.expressNoTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_express_no_tv);
		this.expressCompanyTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_express_company_tv);
		if (expiration > 0) {
			modifyExpressTv.setVisibility(VISIBLE);
			queryExpressTv.setVisibility(VISIBLE);
			cancelTv.setVisibility(VISIBLE);
		}
		initData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.shopsdk_refund_detail_modify_express_tv) {
			gotoFillInExpressPage();
		} else if (v.getId() == R.id.shopsdk_refund_detail_query_express_tv) {
			gotoExpressDetailPage();
		}
	}

	private void initData() {
		this.expressNoTv.setText(refundDescription.getExpressNo());
		this.expressCompanyTv.setText(refundDescription.getExpressCompanyName());
	}

	private void gotoFillInExpressPage() {
		FillInExpressNumberPage fillInExpressNumberPage = new FillInExpressNumberPage(pageAdapter.getPage().getTheme(), orderCommodityId);
//		fillInExpressNumberPage.show(getContext(), null);
		fillInExpressNumberPage.showForResult(pageAdapter.getPage().getContext(), null, new FakeActivity() {
			@Override
			public void onResult(HashMap<String, Object> data) {
				super.onResult(data);
				if (data != null && data.containsKey("isSubmit")) {
					if ((Boolean)data.get("isSubmit")) {
						if (onRequestRefundDetailListener != null) {
							onRequestRefundDetailListener.onRequestRefundDetail();
						}
					}
				}
			}
		});
	}

	private void gotoExpressDetailPage() {
		TransportMsgPage transportMsgPage = new TransportMsgPage(pageAdapter.getPage().getTheme(), refundDescription.getTransportId());
		transportMsgPage.show(pageAdapter.getPage().getContext(), null);
	}
}
