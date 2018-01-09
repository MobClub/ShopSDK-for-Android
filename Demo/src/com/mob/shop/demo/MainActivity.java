package com.mob.shop.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.shop.datatype.entity.Order;
import com.mob.shop.demo.util.PayHelper;
import com.mob.shop.gui.Callback;
import com.mob.shop.gui.ShopGUI;
import com.mob.shop.gui.pay.customizedpay.CustomizedPayListener;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity implements View.OnClickListener {
	private TextView ignoreTv;
	private TextView mobPayTv;
	private TextView customizedPayTv;
	private boolean enableMyPay = false;
	private Callback callback;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		/* ShopSDK/ShopGUI不提供用户信息的持久化缓存，开发者需自行实现，
		 * 建议每次启动应用时，获取当前用户信息并调用MobSDK.setUser设置给ShopSDK，若不设置，
		 * 当前用户将处于匿名状态，除“查看商品”、“添加购物车”等个别功能外，其他功能不可用
		 */
		if (UMSSDK.amILogin()) {
			UMSSDK.getLoginUser(new OperationCallback<User>() {
				@Override
				public void onSuccess(User user) {
					String id = user.id.get();
					String nickname = user.nickname.get();
					String avatar = user.avatar.get()[0];
					MobSDK.setUser(id, nickname, avatar, null);
				}
			});
		}
	}

	private void initView() {
		ignoreTv = (TextView)findViewById(R.id.shop_demo_ignore_tv);
		ignoreTv.setOnClickListener(this);
		mobPayTv = (TextView)findViewById(R.id.shop_demo_mobpay_tv);
		mobPayTv.setOnClickListener(this);
		customizedPayTv = (TextView)findViewById(R.id.shop_demo_customizedpay_tv);
		customizedPayTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int which = v.getId();
		if (which == R.id.shop_demo_ignore_tv || which == R.id.shop_demo_mobpay_tv) {
			enableMyPay = false;
		} else if (which == R.id.shop_demo_customizedpay_tv) {
			enableMyPay = true;
		}
		gotoShopPage();
	}

	private void gotoShopPage() {
		callback = new Callback() {
			@Override
			public void login() {
				// 跳转开发者应用的登录界面，等待用户进行登录操作，登录成功后调用MobSDK.setUser设置登录用户信息
				UMSGUI.showLogin(new OperationCallback<User>() {
					@Override
					public void onSuccess(User user) {
						if (user != null) {
							MobSDK.setUser(user.id.get(), user.nickname.get(), !user.avatar.isNull() ? user.avatar.get
									()[0] : "", new HashMap<String, Object>());
							finish();
						}
					}

					@Override
					public void onFailed(Throwable throwable) {
						super.onFailed(throwable);
						Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void logout() {
				// 跳转开发者应用的退出登录界面，等待用户进行退出操作，退出成功后调用MobSDK.clearUser清除用户信息
				UMSSDK.logout(new OperationCallback<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
						super.onSuccess(aVoid);
						MobSDK.clearUser();
					}

					@Override
					public void onFailed(Throwable throwable) {
						super.onFailed(throwable);
						Toast.makeText(MainActivity.this, "账户登出失败", Toast.LENGTH_SHORT).show();
					}
				});
			}

			/**
			 * 自有支付回调，若要使用自有支付，需在该回调中实现支付流程，并将支付结果通知ShopGUI
			 *
			 * 说明：
			 * 1.不使用自有支付时，请勿重写该回调方法，更不能在方法中执行耗时的操作；
			 *
			 * @param order 待支付订单
			 * @param listener 自由支付监听器，用于向ShopGUI通知支付结果
			 * @return 是否使用自有支付， true：使用自有支付； false：使用MobPay支付；默认false
			 */
			@Override
			public boolean pay(Order order, CustomizedPayListener listener) {
				if (enableMyPay) {
					// 自有支付流程
					PayHelper.getInstance(getApplicationContext()).pay(order, listener);
				}
				return enableMyPay;
			}
		};
		// 进入商城首页
		ShopGUI.showShopPage(callback);
		finish();
	}
}