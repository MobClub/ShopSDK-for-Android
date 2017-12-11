package com.mob.shop.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.shop.gui.Callback;
import com.mob.shop.gui.ShopGUI;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;

import java.util.HashMap;

public class MainActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Callback callback = new Callback() {
			@Override
			public void login() {
				// 跳转开发者应用的登录界面，等待用户进行登录操作，登录成功后调用MobSDK.setUser设置登录用户信息
				UMSGUI.showLogin(new com.mob.ums.OperationCallback<User>() {
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
		};

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

		// 进入商城首页
		ShopGUI.showShopPage(callback);
		finish();
	}
}