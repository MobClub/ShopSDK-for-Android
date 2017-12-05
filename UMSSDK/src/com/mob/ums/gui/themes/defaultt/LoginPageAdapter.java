package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import com.mob.ums.OperationCallback;
import com.mob.ums.SocialNetwork;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.LoginPage;
import com.mob.ums.gui.pages.RegisterPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.PasswordView;
import com.mob.ums.gui.themes.defaultt.components.PhoneView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

import java.util.HashMap;

public class LoginPageAdapter extends DefaultThemePageAdapter<LoginPage> implements OnClickListener {
	private PhoneView llPhone;
	private PasswordView llPassword;
	private TextView tvLogin;
	private TextView tvGoRegister;
	private OperationCallback<User> callback;
	private ProgressDialog pd;
	
	public void onCreate(LoginPage page, Activity activity) {
		super.onCreate(page, activity);
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initPage(activity);
		initCallback();
	}
	
	private void initPage(final Activity activity) {
		final LinearLayout llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);
		
		// title
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			protected void onReturn() {
				finish();
			}
			
			protected String getTitleResName() {
				return "umssdk_default_login";
			}
		}, lp);
		
		// phone and password
		LinearLayout llBody = new LinearLayout(activity);
		llBody.setOrientation(LinearLayout.VERTICAL);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		int dp15 = ResHelper.dipToPx(activity, 15);
		lp.leftMargin = dp15;
		lp.rightMargin = dp15;
		llPage.addView(llBody, lp);
		
		llPhone = new PhoneView(getPage().getTheme());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llPhone, lp);
		
		llPassword = new PasswordView(getPage().getTheme());
		llPassword.showForgetPassword();
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llPassword, lp);
		
		// login button
		RelativeLayout rl = new RelativeLayout(activity);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llBody.addView(rl, lp);
		
		tvLogin = new TextView(activity);
		tvLogin.setId(1);
		tvLogin.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_login_btn"));
		tvLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		tvLogin.setTextColor(0xffffffff);
		tvLogin.setText(ResHelper.getStringRes(activity, "umssdk_default_login"));
		tvLogin.setGravity(Gravity.CENTER);
		tvLogin.setOnClickListener(this);
		RelativeLayout.LayoutParams lprl = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 45));
		lprl.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(tvLogin, lprl);
		
		tvGoRegister = new TextView(activity);
		tvGoRegister.setId(2);
		tvGoRegister.setTextColor(0xffe4554c);
		tvGoRegister.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvGoRegister.setText(ResHelper.getStringRes(activity, "umssdk_default_go_to_register"));
		tvGoRegister.setOnClickListener(this);
		lprl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lprl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lprl.addRule(RelativeLayout.BELOW, tvLogin.getId());
		lprl.topMargin = ResHelper.dipToPx(activity, 20);
		rl.addView(tvGoRegister, lprl);
		
		TextView tv = new TextView(activity);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setTextColor(0xff979797);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_dont_have_account"));
		lprl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lprl.addRule(RelativeLayout.LEFT_OF, tvGoRegister.getId());
		lprl.addRule(RelativeLayout.ALIGN_TOP, tvGoRegister.getId());
		lprl.rightMargin = ResHelper.dipToPx(activity, 7);
		rl.addView(tv, lprl);
		
		// find social networks
		new Thread() {
			public void run() {
				final SocialNetwork[] sns = UMSSDK.getAvailableSocialLoginWays();
				if (sns != null && sns.length > 0) {
					UIHandler.sendEmptyMessage(0, new Callback() {
						public boolean handleMessage(Message msg) {
							initSocialLogin(activity, llPage, sns);
							return false;
						}
					});
				}
			}
		}.start();
	}
	
	private void initSocialLogin(Activity activity, LinearLayout llPage, SocialNetwork[] sns) {
		final LinearLayout llSocialLogin = new LinearLayout(activity);
		llSocialLogin.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(llSocialLogin, lp);
		
		RelativeLayout rl = new RelativeLayout(activity);
		int dp15 = ResHelper.dipToPx(activity, 15);
		rl.setPadding(dp15, 0, dp15, 0);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llSocialLogin.addView(rl, lp);
		
		View vSep = new View(activity);
		vSep.setBackgroundColor(0xff979797);
		RelativeLayout.LayoutParams lprl = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 1));
		lprl.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(vSep, lprl);
		
		TextView tv = new TextView(activity);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setTextColor(0xff979797);
		tv.setBackgroundColor(0xffffffff);
		int dp25 = ResHelper.dipToPx(activity, 25);
		tv.setPadding(dp25, 0, dp25, 0);
		tv.setText(ResHelper.getStringRes(activity, "umssdk_default_the_other_laogin_ways"));
		lprl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lprl.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(tv, lprl);
		
		LinearLayout ll = new LinearLayout(activity);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int dp34 = ResHelper.dipToPx(activity, 34);
		lp.topMargin = dp34;
		lp.bottomMargin = dp34;
		llSocialLogin.addView(ll, lp);
		
		int dp2 = ResHelper.dipToPx(activity, 2);
		for (int i = 0; i < sns.length; i++) {
			ImageView iv = new ImageView(activity);
			iv.setTag(sns[i]);
			String resName = "umssdk_default_" + sns[i].getName().toLowerCase();
			iv.setPadding(dp25, 0, dp25, 0);
			iv.setImageResource(ResHelper.getBitmapRes(activity, resName));
			iv.setScaleType(ScaleType.CENTER_INSIDE);
			iv.setOnClickListener(this);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			lp.leftMargin = (i == 0) ? 0 : dp2;
			lp.rightMargin = (i == sns.length - 1) ? 0 : dp2;
			ll.addView(iv, lp);
		}
	}
	
	private void initCallback() {
		callback = new OperationCallback<User>() {
			public void onSuccess(User data) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				getPage().finishOnSuccess(data);
			}
			
			public void onCancel() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
			}
			
			public void onFailed(Throwable t) {
//				t.printStackTrace();
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				ErrorDialog.Builder builder = new ErrorDialog.Builder(getPage().getContext(), getPage().getTheme());
				int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_login");
				builder.setTitle(getPage().getContext().getString(resId));
				builder.setThrowable(t);
				builder.setMessage(t.getMessage());
				builder.show();
			}
		};
	}
	
	public void onClick(View v) {
		if (v.equals(tvLogin)) {
			String phone = llPhone.getPhone();
			String password = llPassword.getPassword();
			if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				pd = new ProgressDialog.Builder(v.getContext(), getPage().getTheme()).show();
				UMSSDK.loginWithPhoneNumber(llPhone.getCountry(), phone, password, callback);
			}
		} else if (v.equals(tvGoRegister)) {
			RegisterPage page = new RegisterPage(getPage().getTheme());
			if (llPhone.getPhone().length() > 0) {
				page.setLoginNumber(llPhone.getPhone());
			}
			page.showForResult(v.getContext(), null, new FakeActivity() {
				public void onResult(HashMap<String, Object> data) {
					if (data != null) {
						getPage().finishOnSuccess((User) data.get("me"));
					}
				}
			});
		} else {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = new ProgressDialog.Builder(v.getContext(), getPage().getTheme()).show();
			SocialNetwork sn = (SocialNetwork) v.getTag();
			UMSSDK.loginWithSocialAccount(sn, callback);
		}
	}
	
}