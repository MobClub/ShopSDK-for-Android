package com.mob.shop.gui.themes.defaultt;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.FillInExpressNumberPage;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;

/**
 * Created by weishj on 2017/10/31.
 */

public class RefundAgreed extends RefundBaseView {
	private TextView receiverTv;
	private TextView phoneTv;
	private TextView refundAddrTv;
	private TextView remarkTv;

	public RefundAgreed(RefundDetailPageAdapter pageAdapter, RefundDescription refundDescription) {
		super(pageAdapter, refundDescription);
	}

	@Override
	protected View getView() {
		LayoutInflater inflater = LayoutInflater.from(pageAdapter.getPage().getContext());
		View view = inflater.inflate(R.layout.shopsdk_default_page_refund_detail_agreed, null);
		return view;
	}

	@Override
	protected String getHeaderTitle() {
		return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
				pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_agreed"));
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
		this.receiverTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_receiver_tv);
		this.phoneTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_phone_tv);
		this.refundAddrTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_refund_addr_tv);
		this.remarkTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_remark_tv);
		if (expiration > 0) {
			fillInExpressTv.setVisibility(VISIBLE);
			cancelTv.setVisibility(VISIBLE);
		}
		initData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.shopsdk_refund_detail_fill_in_express_tv) {
			gotoFillInExpressPage();
		}
	}

	private void initData() {
		this.receiverTv.setText(refundDescription.getReceiver());
		this.phoneTv.setText(refundDescription.getReceiverTelephone());
		this.refundAddrTv.setText(refundDescription.getReceiverAddress());
		this.remarkTv.setText(refundDescription.getRemark());
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
}
