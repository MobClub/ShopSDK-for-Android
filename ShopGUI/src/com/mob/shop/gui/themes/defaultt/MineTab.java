package com.mob.shop.gui.themes.defaultt;


import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.biz.ShopUser;
import com.mob.shop.datatype.OrderStatus;
import com.mob.shop.datatype.entity.OrderStatistic;
import com.mob.shop.gui.Callback;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.AboutMePage;
import com.mob.shop.gui.pages.MainPage;
import com.mob.shop.gui.pages.MineCouponsPage;
import com.mob.shop.gui.pages.MyOrdersPage;
import com.mob.shop.gui.pages.RefundListPage;
import com.mob.shop.gui.pages.dialog.OKCancelDialog;
import com.mob.shop.gui.tabs.Tab;
import com.mob.shop.gui.themes.defaultt.components.RollImageView;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;
import java.util.List;

public class MineTab implements Tab, View.OnClickListener {
	private static final String TAG = "MineTab";
	private MainPage mainPage;
	private Context context;
	private TextView tvNickName;
	private TextView tvUnPayCount;
	private RelativeLayout rlCoupon;
	private TextView tvUnSeedCount;
	private TextView tvUnShippingCount;
	private RelativeLayout rlUnPay;
	private RelativeLayout rlUnSeed;
	private RelativeLayout rlUnShipping;
	private RelativeLayout rlRefund;
	private RelativeLayout rlAbout;
	private RelativeLayout rlHelp;
	private boolean anonymous = true;
	private RollImageView ivLogo;
	private ShopUser mUser;

	public MineTab(MainPage mainPage) {
		this.mainPage = mainPage;
	}

	@Override
	public String getSelectedIconResName() {
		return "shopsdk_default_orange_mine";
	}

	@Override
	public String getUnselectedIconResName() {
		return "shopsdk_default_grey_mine";
	}

	@Override
	public String getTitleResName() {
		return "shopsdk_default_mine";
	}

	@Override
	public String getSelectedTitleColor() {
		return "select_tab";
	}

	@Override
	public String getUnselectedTitleColor() {
		return "unselect_tab";
	}

	@Override
	public View getTabView(final Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_tab_mine, null);
		RelativeLayout rlMyOrders = (RelativeLayout) view.findViewById(R.id.rlMyOrders);
		rlMyOrders.setOnClickListener(this);
		tvNickName = (TextView) view.findViewById(R.id.tvNickName);
		tvUnPayCount = (TextView) view.findViewById(R.id.tvUnPayCount);
		tvUnSeedCount = (TextView) view.findViewById(R.id.tvUnSeedCount);
		tvUnShippingCount = (TextView) view.findViewById(R.id.tvUnShippingCount);
		rlUnPay = (RelativeLayout) view.findViewById(R.id.rlUnPay);
		rlUnSeed = (RelativeLayout) view.findViewById(R.id.rlUnSeed);
		rlUnShipping = (RelativeLayout) view.findViewById(R.id.rlUnShipping);
		rlCoupon = (RelativeLayout) view.findViewById(R.id.rlCoupon);
		rlRefund = (RelativeLayout) view.findViewById(R.id.rlRefund);
		rlAbout = (RelativeLayout) view.findViewById(R.id.rlAbout);
		rlHelp = (RelativeLayout) view.findViewById(R.id.rlHelp);
		ivLogo = (RollImageView) view.findViewById(R.id.ivLogo);
		rlCoupon.setOnClickListener(this);
		rlUnPay.setOnClickListener(this);
		rlUnSeed.setOnClickListener(this);
		rlUnShipping.setOnClickListener(this);
		rlRefund.setOnClickListener(this);
		rlAbout.setOnClickListener(this);
		rlHelp.setOnClickListener(this);

		ivLogo.drawBorder(0x7fffffff);
		ivLogo.setRound(ResHelper.dipToPx(context, 60));
		ivLogo.execute(null, ResHelper.getBitmapRes(context, "shopsdk_default_user_logo"));
		ivLogo.setOnClickListener(this);

		ShopSDK.bindUserWatcher(new ShopSDK.UserWatcher() {
			@Override
			public void onUserStateChange(ShopUser user) {
				mUser = user;
				initData();
			}
		});
		mUser = ShopSDK.getUser();

		return view;
	}

	private void initData() {
		anonymous = mUser.isAnonymous();
		if (anonymous) {
			initUserInfo();
			return;
		}
		ivLogo.execute(mUser.getAvatar(), ResHelper.getBitmapRes(context, "shopsdk_default_user_logo"));
		if (!TextUtils.isEmpty(mUser.getNickname())) {
			tvNickName.setText(mUser.getNickname());
		} else {
			HashMap<String, Object> extraInfo = mUser.getExtra();
			if (extraInfo != null && extraInfo.containsKey("phone")) {
				String phone = (String) extraInfo.get("phone");
				if (!TextUtils.isEmpty(phone)) {
					tvNickName.setText(phone.substring(0, 3) + "****" + phone.substring(8));
				}
			} else {
				tvNickName.setText(ResHelper.getStringRes(context, "shopsdk_default_nickname"));
			}
		}
		getOrderInfoCount();
	}

	private void getOrderInfoCount() {
		ShopSDK.getOrdersStatisticInfo(new SGUIOperationCallback<List<OrderStatistic>>(mainPage.getCallback()) {
			@Override
			public void onSuccess(List<OrderStatistic> data) {
				super.onSuccess(data);
				if (data == null || data.size() == 0) {
					resetCount();
					return;
				}
				for (OrderStatistic orderStatistic : data) {
					orderStatusCount(orderStatistic);
				}
			}

			@Override
			public boolean onResultError(Throwable t) {
				return super.onResultError(t);
			}
		});
	}

	private void initUserInfo() {
		ivLogo.execute(null, ResHelper.getBitmapRes(context, "shopsdk_default_user_logo"));
		tvNickName.setText(ResHelper.getStringRes(context, "shopsdk_default_nickname"));
		resetCount();
	}

	private void resetCount() {
		tvUnPayCount.setVisibility(View.GONE);
		tvUnSeedCount.setVisibility(View.GONE);
		tvUnShippingCount.setVisibility(View.GONE);
	}

	private void orderStatusCount(OrderStatistic orderStatistic) {
		switch (orderStatistic.getStatus()) {
			case CREATED_AND_WAIT_FOR_PAY:
				if (orderStatistic.getCount() > 0) {
					tvUnPayCount.setText(String.valueOf(orderStatistic.getCount()));
					tvUnPayCount.setVisibility(View.VISIBLE);
				} else {
					tvUnPayCount.setVisibility(View.GONE);
				}
				break;
			case PAID_AND_WAIT_FOR_DILIVER:
				if (orderStatistic.getCount() > 0) {
					tvUnSeedCount.setText(String.valueOf(orderStatistic.getCount()));
					tvUnSeedCount.setVisibility(View.VISIBLE);
				} else {
					tvUnSeedCount.setVisibility(View.GONE);
				}
				break;
			case DILIVERED_AND_WAIT_FOR_RECEIPT:
				if (orderStatistic.getCount() > 0) {
					tvUnShippingCount.setText(String.valueOf(orderStatistic.getCount()));
					tvUnShippingCount.setVisibility(View.VISIBLE);
				} else {
					tvUnShippingCount.setVisibility(View.GONE);
				}
				break;
		}
	}

	@Override
	public void onSelected() {
		initData();
	}

	@Override
	public void onUnselected() {
		ShopSDK.unbindUserWatcher();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.rlUnPay) {
			gotoMyOrderPage(OrderStatus.CREATED_AND_WAIT_FOR_PAY.getValue());
		} else if (v.getId() == R.id.rlMyOrders) {
			gotoMyOrderPage(OrderStatus.ALL.getValue());
		} else if (v.getId() == R.id.rlCoupon) {
			if (anonymous) {
				Callback callback = mainPage.getCallback();
				if (callback != null) {
					callback.login();
				}
			} else {
				MineCouponsPage mineCouponsPage = new MineCouponsPage(mainPage.getTheme());
				mineCouponsPage.show(mainPage.getContext(), null);
			}
		} else if (v.getId() == R.id.rlUnSeed) {
			gotoMyOrderPage(OrderStatus.PAID_AND_WAIT_FOR_DILIVER.getValue());
		} else if (v.getId() == R.id.rlUnShipping) {
			gotoMyOrderPage(OrderStatus.DILIVERED_AND_WAIT_FOR_RECEIPT.getValue());
		} else if (v.getId() == R.id.rlRefund) {
			if (anonymous) {
				Callback callback = mainPage.getCallback();
				if (callback != null) {
					callback.login();
				}
			} else {
				RefundListPage refundListPage = new RefundListPage(mainPage.getTheme(), "");
				refundListPage.show(mainPage.getContext(), null);
			}
		} else if (v.getId() == R.id.ivLogo) {
			if (anonymous) {
				Callback callback = mainPage.getCallback();
				if (callback != null) {
					callback.login();
				}
			} else {
				OKCancelDialog.Builder builder = new OKCancelDialog.Builder(mainPage.getContext(), mainPage.getTheme
						());
				int resId = ResHelper.getStringRes(mainPage.getContext(), "shopsdk_default_logout_tip");
				builder.setMessage(mainPage.getContext().getString(resId));
				builder.noPadding();
				builder.setOnClickListener(new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == DialogInterface.BUTTON_POSITIVE) {
							Callback callback = mainPage.getCallback();
							if (callback != null) {
								callback.logout();
							}
						}
						dialog.dismiss();
					}
				});
				builder.show();
			}
		} else if (v.getId() == R.id.rlAbout) {
			AboutMePage aboutMePage = new AboutMePage(mainPage.getTheme());
			aboutMePage.show(mainPage.getContext(),null);
		} else if (v.getId() == R.id.rlHelp) {
			mainPage.toastMessage(ResHelper.getStringRes(mainPage.getContext(),"shopsdk_default_unopen"));
		}
	}

	private void gotoMyOrderPage(int status) {
		if (anonymous) {
			Callback callback = mainPage.getCallback();
			if (callback != null) {
				callback.login();
			}
			return;
		}
		MyOrdersPage myOrdersPage = new MyOrdersPage(mainPage.getTheme(), status);
		myOrdersPage.show(mainPage.getContext(), null);
	}

}
