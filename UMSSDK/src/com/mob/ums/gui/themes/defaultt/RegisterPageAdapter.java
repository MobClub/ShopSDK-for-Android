package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.gui.pages.PostRegisterPage;
import com.mob.ums.gui.pages.RegisterPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.OKDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;
import com.mob.ums.gui.themes.defaultt.components.PasswordView;
import com.mob.ums.gui.themes.defaultt.components.PhoneView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;
import com.mob.ums.gui.themes.defaultt.components.VCodeView;

import java.util.HashMap;

public class RegisterPageAdapter extends DefaultThemePageAdapter<RegisterPage> {
	private LinearLayout llPage;
	private VCodeView llVCode;
	private TextView tvCountDown;
	private ProgressDialog pd;

	public void onCreate(RegisterPage page, Activity activity) {
		super.onCreate(page, activity);
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initPage(activity);
	}
	
	private void initPage(final Activity activity) {
		llPage = new LinearLayout(activity);
		llPage.setOrientation(LinearLayout.VERTICAL);
		activity.setContentView(llPage);
		
		// title
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llPage.addView(new TitleView(activity) {
			protected void onReturn() {
				finish();
			}
			
			protected String getTitleResName() {
				if (getPage().isForgetPasswrodType()) {
					return "umssdk_default_reset_passwrod";
				} else if (getPage().isBindPhoneType()) {
					return "umssdk_default_bind_phone";
				} else {
					return "umssdk_default_register";
				}
			}
		}, lp);
		
		// phone, verify code and password
		LinearLayout llBody = new LinearLayout(activity);
		llBody.setOrientation(LinearLayout.VERTICAL);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		int dp15 = ResHelper.dipToPx(activity, 15);
		lp.leftMargin = dp15;
		lp.rightMargin = dp15;
		llPage.addView(llBody, lp);
		
		final PhoneView llPhone = new PhoneView(getPage().getTheme());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llPhone, lp);
		llPhone.setPhone(getPage().getLoginNumber());
		
		llVCode = new VCodeView(getPage()) {
			protected String getContry() {
				return llPhone.getCountry();
			}
			
			protected String getPhone() {
				return llPhone.getPhone();
			}
			
			protected boolean beforeSend() {
				tvCountDown.setVisibility(INVISIBLE);
				return (llPhone.getPhone().length() <= 0);
			}
			
			protected boolean isRegiterType() {
				return !getPage().isForgetPasswrodType();
			}
			
			protected void afterSend(boolean smartVCode) {
				if (smartVCode) {
					OKDialog.Builder builder = new OKDialog.Builder(activity, getPage().getTheme());
					int resId = ResHelper.getStringRes(getContext(), "umssdk_default_send_vcode");
					builder.setTitle(getContext().getString(resId));
					resId = ResHelper.getStringRes(getContext(), "umssdk_default_smart_verified");
					builder.setMessage(getContext().getString(resId));
					builder.show();
				} else {
					tvCountDown.setVisibility(VISIBLE);
				}
			}
			
			protected void onCountDown(int secondsLeft) {
				if (secondsLeft > 0) {
					int resId = ResHelper.getStringRes(activity, "umssdk_default_x_seconds_to_receive_vcode");
					String msg = activity.getString(resId, secondsLeft);
					tvCountDown.setTextColor(0xff979797);
					tvCountDown.setText(msg);
					tvCountDown.setOnClickListener(null);
				} else {
					tvCountDown.setTextColor(0xffe4554c);
					tvCountDown.setText(ResHelper.getStringRes(activity, "umssdk_default_can_not_receive_vcode"));
					tvCountDown.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							askToSend();
						}
					});
				}
			}
		};
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llVCode, lp);
		
		final PasswordView llPassword = new PasswordView(getPage().getTheme());
		if (getPage().isForgetPasswrodType()) {
			llPassword.setHintText("umssdk_default_enter_new_password");
		} else if (getPage().isBindPhoneType()) {
			llPassword.setHintText("umssdk_default_bind_phone_hint");
		}
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		llBody.addView(llPassword, lp);
		
		tvCountDown = new TextView(activity);
		tvCountDown.setTextColor(0xff979797);
		tvCountDown.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvCountDown.setGravity(Gravity.CENTER);
		tvCountDown.setVisibility(View.INVISIBLE);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int dp7 = ResHelper.dipToPx(activity, 7);
		lp.topMargin = dp7;
		llBody.addView(tvCountDown, lp);
		
		// action button
		TextView tv = new TextView(activity);
		tv.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_login_btn"));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tv.setTextColor(0xffffffff);
		if (getPage().isForgetPasswrodType() || getPage().isBindPhoneType()) {
			tv.setText(ResHelper.getStringRes(activity, "umssdk_default_confirm"));
		} else {
			tv.setText(ResHelper.getStringRes(activity, "umssdk_default_next"));
		}
		tv.setGravity(Gravity.CENTER);
		tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (getPage().isForgetPasswrodType()) {
					goResetPassword(llPhone.getCountry(), llPhone.getPhone(),
							llVCode.getVCode(), llPassword.getPassword());
				} else if (getPage().isBindPhoneType()) {
					goBindPhone(llPhone.getCountry(), llPhone.getPhone(),
							llVCode.getVCode(), llPassword.getPassword());
				} else {
					goRegister(llPhone.getCountry(), llPhone.getPhone(),
							llVCode.getVCode(), llPassword.getPassword());
				}
			}
		});
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 45));
		lp.topMargin = ResHelper.dipToPx(activity, 60);
		llBody.addView(tv, lp);
		
		View vSep = new View(activity);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		llBody.addView(vSep, lp);
		
		// return login
		if (!getPage().isForgetPasswrodType() && !getPage().isBindPhoneType()) {
			LinearLayout ll = new LinearLayout(activity);
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, ResHelper.dipToPx(activity, 56));
			lp.gravity = Gravity.CENTER;
			llBody.addView(ll, lp);
			ll.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
			
			tv = new TextView(activity);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			tv.setTextColor(0xff979797);
			tv.setText(ResHelper.getStringRes(activity, "umssdk_default_already_have_account"));
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			ll.addView(tv, lp);
			
			tv = new TextView(activity);
			tv.setTextColor(0xffe4554c);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
			tv.setText(ResHelper.getStringRes(activity, "umssdk_default_go_to_login"));
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.leftMargin = dp7;
			ll.addView(tv, lp);
		}
	}
	
	private void goResetPassword(String country, String phone, String vcode, String password) {
		if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
			UMSSDK.resetPasswordWithPhoneNumber(country, phone, vcode, password, new OperationCallback<Void>() {
				public void onSuccess(Void data) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					OKDialog.Builder builder = new OKDialog.Builder(getPage().getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_verified");
					builder.setTitle(getPage().getContext().getString(resId));
					resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_use_new_password_to_login");
					builder.setMessage(getPage().getContext().getString(resId));
					builder.setOnDismissListener(new OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							finish();
						}
					});
					builder.show();
				}
				
				public void onFailed(Throwable t) {
//				t.printStackTrace();
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getPage().getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_reset_passwrod");
					builder.setTitle(getPage().getContext().getString(resId));
					builder.setMessage(t.getMessage());
					builder.setThrowable(t);
					builder.show();
				}
			});
		}
	}
	
	private void goBindPhone(String country, String phone, String vcode, String password) {
		if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
			UMSSDK.bindPhone(country, phone, vcode, password, new OperationCallback<Void>() {
				public void onSuccess(Void data) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					int resid = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_bind_success");
					Toast.makeText(getPage().getContext(), resid, Toast.LENGTH_SHORT).show();
					getPage().setResult(new HashMap<String, Object>());
					finish();
				}
				
				public void onFailed(Throwable t) {
//				t.printStackTrace();
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getPage().getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_bind_phone");
					builder.setTitle(getPage().getContext().getString(resId));
					builder.setMessage(t.getMessage());
					builder.setThrowable(t);
					builder.show();
				}
			});
		}
	}
	
	private void goRegister(String country, String phone, String vcode, String password) {
		if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = new ProgressDialog.Builder(getPage().getContext(), getPage().getTheme()).show();
			UMSSDK.registerWithPhoneNumber(country, phone, vcode, password, null, new OperationCallback<User>() {
				public void onSuccess(User me) {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					HashMap<String, Object> res = new HashMap<String, Object>();
					res.put("me", me);
					getPage().setResult(res);
					goPostRegister(me);
				}
				
				public void onFailed(Throwable t) {
//					t.printStackTrace();
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					ErrorDialog.Builder builder = new ErrorDialog.Builder(getPage().getContext(), getPage().getTheme());
					int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_register");
					builder.setTitle(getPage().getContext().getString(resId));
					builder.setMessage(t.getMessage());
					builder.setThrowable(t);
					builder.show();
				}
			});
		}
	}
	
	private void goPostRegister(final User me) {
		OKDialog.Builder builder = new OKDialog.Builder(getPage().getContext(), getPage().getTheme());
		int resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_verified");
		builder.setTitle(getPage().getContext().getString(resId));
		resId = ResHelper.getStringRes(getPage().getContext(), "umssdk_default_vcode_correct");
		builder.setMessage(getPage().getContext().getString(resId));
		builder.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				PostRegisterPage page = new PostRegisterPage(getPage().getTheme());
				page.setUser(me);
				page.showForResult(getPage().getContext(), null, new FakeActivity() {
					public void onResult(HashMap<String, Object> data) {
						getPage().finish();
					}
				});
			}
		});
		builder.show();
	}
	
}
