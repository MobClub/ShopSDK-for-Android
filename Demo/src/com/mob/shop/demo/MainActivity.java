package com.mob.shop.demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.mob.MobSDK;
import com.mob.shop.gui.Callback;
import com.mob.shop.gui.ShopGUI;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.UMSGUI;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Callback callback = new Callback() {
			@Override
			public void login() {
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


		final String userToken = UMSSDK.getLoginUserToken();
		if (!TextUtils.isEmpty(userToken)) {
			UMSSDK.getUserListByIDs(new String[]{UMSSDK.getLoginUserId()}, new OperationCallback<ArrayList<User>>() {
				@Override
				public void onSuccess(ArrayList<User> users) {
					super.onSuccess(users);
					if (users != null && !users.isEmpty()) {
						MobSDK.setUser(users.get(0).id.get(), users.get(0).nickname.get(), !users.get(0).avatar.isNull() ? users.get(0).avatar.get
								()[0] : "", new HashMap<String, Object>());
					}
				}
				@Override
				public void onFailed(Throwable throwable) {
					super.onFailed(throwable);
				}
			});
		}
		ShopGUI.showShopPage(callback);
		finish();
	}
}
