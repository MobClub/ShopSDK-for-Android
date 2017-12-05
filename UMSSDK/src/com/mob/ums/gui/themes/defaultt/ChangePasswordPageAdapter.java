package com.mob.ums.gui.themes.defaultt;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.gui.pages.ChangePasswordPage;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.OKDialog;
import com.mob.ums.gui.themes.defaultt.components.PasswordView;
import com.mob.ums.gui.themes.defaultt.components.TitleView;

public class ChangePasswordPageAdapter extends DefaultThemePageAdapter<ChangePasswordPage> {
	
	public void onCreate(ChangePasswordPage page, Activity activity) {
		super.onCreate(page, activity);
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initPage(activity);
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
				return "umssdk_default_change_password";
			}
		}, lp);
		
		// inputs
		final PasswordView llOldPassword = new PasswordView(getPage().getTheme());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int dp15 = ResHelper.dipToPx(activity, 15);
		lp.leftMargin = dp15;
		lp.rightMargin = dp15;
		llPage.addView(llOldPassword, lp);
		
		ImageView iv = (ImageView) llOldPassword.findViewById(1);
		iv.setImageResource(ResHelper.getBitmapRes(activity, "umssdk_default_old_password"));
		EditText et = (EditText)  llOldPassword.findViewById(2);
		et.setHint(ResHelper.getStringRes(activity, "umssdk_default_enter_old_password"));
		
		final PasswordView llNewPassword = new PasswordView(getPage().getTheme());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = dp15;
		lp.rightMargin = dp15;
		llPage.addView(llNewPassword, lp);
		et = (EditText)  llNewPassword.findViewById(2);
		et.setHint(ResHelper.getStringRes(activity, "umssdk_default_enter_new_password"));
		
		final PasswordView llPasswordConf = new PasswordView(getPage().getTheme());
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = dp15;
		lp.rightMargin = dp15;
		llPage.addView(llPasswordConf, lp);
		et = (EditText)  llPasswordConf.findViewById(2);
		et.setHint(ResHelper.getStringRes(activity, "umssdk_default_confirm_new_password"));
		
		// button
		TextView tvLogin = new TextView(activity);
		tvLogin.setBackgroundResource(ResHelper.getBitmapRes(activity, "umssdk_default_login_btn"));
		tvLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		tvLogin.setTextColor(0xffffffff);
		tvLogin.setText(ResHelper.getStringRes(activity, "umssdk_default_confirm"));
		tvLogin.setGravity(Gravity.CENTER);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, ResHelper.dipToPx(activity, 45));
		lp.leftMargin = dp15;
		lp.rightMargin = dp15;
		lp.topMargin = lp.height * 2;
		llPage.addView(tvLogin, lp);
		tvLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String oldPsw = llOldPassword.getPassword();
				String newPsw = llNewPassword.getPassword();
				String pswCon = llPasswordConf.getPassword();
				if (oldPsw.length() > 0 && newPsw.length() > 0) {
					if (newPsw.equals(pswCon)) {
						UMSSDK.changePassword(newPsw, oldPsw, new OperationCallback<Void>() {
							public void onSuccess(Void data) {
								OKDialog.Builder builder = new OKDialog.Builder(activity, getPage().getTheme());
								int resId = ResHelper.getStringRes(activity, "umssdk_default_change_password");
								builder.setTitle(activity.getString(resId));
								resId = ResHelper.getStringRes(activity, "umssdk_default_use_new_password_to_login");
								builder.setMessage(activity.getString(resId));
								builder.setOnDismissListener(new OnDismissListener() {
									public void onDismiss(DialogInterface dialog) {
										finish();
									}
								});
								builder.show();
							}
							
							public void onFailed(Throwable t) {
//								t.printStackTrace();
								ErrorDialog.Builder builder = new ErrorDialog.Builder(activity, getPage().getTheme());
								int resId = ResHelper.getStringRes(activity, "umssdk_default_change_password");
								builder.setTitle(activity.getString(resId));
								builder.setThrowable(t);
								builder.setMessage(t.getMessage());
								builder.show();
							}
						});
					} else {
						Context ctx = getPage().getContext();
						ErrorDialog.Builder builder = new ErrorDialog.Builder(ctx, getPage().getTheme());
						int resId = ResHelper.getStringRes(ctx, "umssdk_default_change_password");
						builder.setTitle(ctx.getString(resId));
						resId = ResHelper.getStringRes(ctx, "umssdk_default_make_sure_new_passwords_are_the_same");
						builder.setMessage(ctx.getString(resId));
						builder.show();
					}
				} else {
					OKDialog.Builder builder = new OKDialog.Builder(activity, getPage().getTheme());
					int resId = ResHelper.getStringRes(activity, "umssdk_default_change_password");
					builder.setTitle(activity.getString(resId));
					resId = ResHelper.getStringRes(activity, "umssdk_default_passwork_format_tip");
					builder.setMessage(activity.getString(resId));
					builder.setOnDismissListener(new OnDismissListener() {
						public void onDismiss(DialogInterface dialog) {
							finish();
						}
					});
					builder.show();
				}
			}
		});
	}
	
}
