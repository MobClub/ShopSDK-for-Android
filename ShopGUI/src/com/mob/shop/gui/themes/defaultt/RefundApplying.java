package com.mob.shop.gui.themes.defaultt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.ApplyRefundPage;
import com.mob.shop.gui.themes.defaultt.components.ExactGridView;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.List;

/**
 * Created by weishj on 2017/10/31.
 */

public class RefundApplying extends RefundBaseView {
	private TextView refundTypeTv;
	private TextView refundReasonTv;
	private TextView refundDescTv;
	private TextView refundFeeTv;
	private ExactGridView refundImgGv;

	public RefundApplying(RefundDetailPageAdapter pageAdapter, RefundDescription refundDescription) {
		super(pageAdapter, refundDescription);
	}

	@Override
	protected View getView() {
		LayoutInflater inflater = LayoutInflater.from(pageAdapter.getPage().getContext());
		View view = inflater.inflate(R.layout.shopsdk_default_page_refund_detail_applying, null);
		return view;
	}

	@Override
	protected String getHeaderTitle() {
		return pageAdapter.getPage().getContext().getString(ResHelper.getStringRes(
				pageAdapter.getPage().getContext(), "shopsdk_default_refund_detail_content_header_applying"));
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
		this.refundTypeTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_type_tv);
		this.refundReasonTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_refund_reason_tv);
		this.refundDescTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_refund_desc_tv);
		this.refundFeeTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_refund_fee_tv);
		this.refundImgGv = (ExactGridView)view.findViewById(R.id.shopsdk_refund_detail_refund_img_gv);
		this.refundImgGv.setAdapter(new RefundImgAdapter(pageAdapter.getPage().getContext(), refundDescription.getRefundImgUrls()));
		if (expiration > 0) {
			modifyRefundTv.setVisibility(VISIBLE);
			cancelTv.setVisibility(VISIBLE);
		}
		initData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.shopsdk_refund_detail_modify_refund_tv) {
			gotoApplyRefundPage();
		}
	}

	private void initData() {
		this.refundTypeTv.setText(refundDescription.getRefundType().getDesc());
		this.refundReasonTv.setText(refundDescription.getRefundReason());
		this.refundDescTv.setText(refundDescription.getRefundDesc());
		this.refundFeeTv.setText(MoneyConverter.conversionMoneyStr(refundDescription.getRefundFee()));
	}

	private void gotoApplyRefundPage() {
		ApplyRefundPage applyRefundPage = new ApplyRefundPage(pageAdapter.getPage().getTheme(), orderCommodityId);
		applyRefundPage.show(getContext(), null);
	}

	private static class RefundImgAdapter extends BaseAdapter {
		private Context context;
		private List<ImgUrl> urls;

		public RefundImgAdapter(Context context, List<ImgUrl> urls) {
			this.context = context;
			this.urls = urls;
		}

		@Override
		public int getCount() {
			return urls == null ? 0 : urls.size();
		}

		@Override
		public ImgUrl getItem(int position) {
			return urls == null ? null : urls.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(ResHelper.getLayoutRes(context,
						"shopsdk_default_page_refund_detail_img_gv_item"), null);
				vh = new ViewHolder();
				vh.iv = (AsyncImageView)convertView.findViewById(R.id.shopsdk_refund_detail_img_iv);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder)convertView.getTag();
			}
			if (urls != null) {
				vh.iv.execute(urls.get(position).getSrc(), ResHelper.getColorRes(context, "order_bg"));
			}

			return convertView;
		}

		private static class ViewHolder {
			private AsyncImageView iv;
		}
	}
}
