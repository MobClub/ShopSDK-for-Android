package com.mob.shop.gui.themes.defaultt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.CouponStatus;
import com.mob.shop.datatype.builder.CouponQuerier;
import com.mob.shop.datatype.entity.Coupon;
import com.mob.shop.gui.R;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.shop.gui.utils.Utils;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weishj on 2017/10/21.
 */

public class CouponListViewAdapter extends DefaultRTRListAdapter {
	private static final int DEFAULT_PAGE_SIZE = 20;

	private List<Coupon> list = new ArrayList<Coupon>();
	private int pageIndex = 1;
	private CouponStatus couponStatus = CouponStatus.RECEIVED_AND_USABLE;

	public CouponListViewAdapter(PullToRequestView view, CouponStatus couponStatus) {
		super(view);
		this.couponStatus = couponStatus;
		// Disable divider of ListView
		getListView().setDivider(null);
	}

	@Override
	protected void onRequest(boolean firstPage) {
		onRequest(firstPage, couponStatus);
	}

	protected void onRequest(boolean firstPage, CouponStatus status) {
		if (firstPage) {
			resetPageIndex();
		}
		getCoupons(pageIndex, status, new MyCallback() {
			@Override
			public void onSuccess(List<Coupon> data) {
				if (pageIndex == 1) {
					setData(data);
				} else {
					appendData(data);
				}
				if (data == null || data.isEmpty()) {
					getParent().lockPullingUp();
				} else {
					pageIndex++;
					getParent().releasePullingUpLock();
				}
			}

			@Override
			public void onFailed(Throwable t) {
				getParent().stopPulling();
			}
		});
	}

	@Override
	public View getView(final int i, View convertView, ViewGroup viewGroup) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_page_my_coupons_item,
					null);
			vh = new ViewHolder();
			vh.priceTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_price_tv);
			vh.descriptionTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_description_tv);
			vh.nameTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_name_tv);
			vh.validTv = (TextView) convertView.findViewById(R.id.shopsdk_my_coupon_valid_tv);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (list != null && !list.isEmpty()) {
			Coupon coupon = list.get(i);
			vh.priceTv.setText(MoneyConverter.conversionString(coupon.getCouponMoney()));
			vh.descriptionTv.setText(coupon.getCouponDescribe());
			vh.nameTv.setText(coupon.getCouponName());
			String format = getContext().getResources().getString(ResHelper.getStringRes(getContext(),
					"shopsdk_default_my_coupon_valid"));
			String validBegin = Utils.long2Time(coupon.getValidBeginAt());
			String validEnd = Utils.long2Time(coupon.getValidEndAt());
			vh.validTv.setText(String.format(format, validBegin, validEnd));
		}

		return convertView;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	public void setData(List<Coupon> data) {
		list = data;
		notifyDataSetChanged();
	}

	public void appendData(List<Coupon> data) {
		if (list == null) {
			list = new ArrayList<Coupon>();
		}
		list.addAll(data);
		notifyDataSetChanged();
	}

	public void clearData() {
		setData(null);
		notifyDataSetChanged();
	}

	public void resetPageIndex() {
		pageIndex = 1;
	}

	public void increasePageIndex() {
		pageIndex ++;
	}

	public void shiftCouponStatus(CouponStatus couponStatus) {
		this.couponStatus = couponStatus;
	}

	class ViewHolder {
		private TextView priceTv;
		private TextView descriptionTv;
		private TextView nameTv;
		private TextView validTv;
	}

	private void getCoupons(int pageIndex, CouponStatus status, final MyCallback callback) {
		ShopSDK.getCoupons(new CouponQuerier(DEFAULT_PAGE_SIZE, pageIndex, status), new OperationCallback<List<Coupon>>() {
			@Override
			public void onSuccess(List<Coupon> data) {
				super.onSuccess(data);
				callback.onSuccess(data);
			}

			@Override
			public void onFailed(Throwable t) {
				super.onFailed(t);
				callback.onFailed(t);
				// TODO For test
//				callback.onSuccess(RawResUtils.getCoupons(getContext()));
			}
		});
	}

	private interface MyCallback {
		void onSuccess(List<Coupon> data);
		void onFailed(Throwable t);
	}
}
