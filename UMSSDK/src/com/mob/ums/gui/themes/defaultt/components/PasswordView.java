package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.jimu.gui.Theme;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.RegisterPage;

public class PasswordView extends LinearLayout {
	private EditText etPassword;
	private TextView tvForget;

	public PasswordView(final Theme theme) {
		super(MobSDK.getContext());
		init(getContext());
		tvForget.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RegisterPage page = new RegisterPage(theme);
				page.setForgetPasswrodType();
				page.show(getContext(), null);
			}
		});
	}

	private void init(Context context) {
		setOrientation(VERTICAL);

		LinearLayout ll = new LinearLayout(context);
		int dp53 = ResHelper.dipToPx(context, 53);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp53);
		addView(ll, lp);

		ImageView iv = new ImageView(context);
		iv.setId(1);
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_password"));
		int dp24 = ResHelper.dipToPx(context, 24);
		lp = new LayoutParams(dp24, dp24);
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(iv, lp);

		etPassword = new EditText(context);
		etPassword.setId(2);
		etPassword.setPadding(0, 0, 0, 0);
		etPassword.setBackground(null);
		etPassword.setSingleLine();
		etPassword.setTextColor(0xff3b3947);
		etPassword.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etPassword.setHintTextColor(0xff969696);
		etPassword.setHint(ResHelper.getStringRes(context, "umssdk_default_enter_password"));
		etPassword.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.leftMargin = dp24;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(etPassword, lp);

		tvForget = new TextView(context);
		tvForget.setTextColor(0xff969696);
		tvForget.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvForget.setText(ResHelper.getStringRes(context, "umssdk_default_forget_password"));
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.leftMargin = dp24;
		ll.addView(tvForget, lp);
		tvForget.setVisibility(GONE);

		View vSep = new View(context);
		vSep.setBackgroundColor(0xffeaeaea);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		addView(vSep, lp);
	}

	public void showForgetPassword() {
		tvForget.setVisibility(VISIBLE);
	}

	public String getPassword() {
		return etPassword.getText().toString().trim();
	}

	public void setHintText(String resName) {
		etPassword.setHint(ResHelper.getStringRes(getContext(), resName));
	}

}
