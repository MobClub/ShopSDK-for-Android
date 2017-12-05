package com.mob.ums.gui.themes.defaultt.components;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.jimu.gui.Theme;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.gui.pages.CountryCodeSelectorePage;

import java.util.HashMap;

public class PhoneView extends LinearLayout {
	private TextView tvCountry;
	private EditText etPhone;
	private ImageView ivClear;

	public PhoneView(final Theme theme) {
		super(MobSDK.getContext());
		init(getContext());
		tvCountry.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CountryCodeSelectorePage page = new CountryCodeSelectorePage(theme);
				page.setLoginCode(getCountry());
				page.showForResult(v.getContext(), null, new FakeActivity() {
					public void onResult(HashMap<String, Object> data) {
						if (data != null) {
							String code = (String) data.get("code");
							tvCountry.setText("+" + code);
						}
					}
				});
			}
		});
	}

	private void init(Context context) {
		setOrientation(VERTICAL);

		LinearLayout ll = new LinearLayout(context);
		int dp53 = ResHelper.dipToPx(context, 53);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, dp53);
		addView(ll, lp);

		int dp24 = ResHelper.dipToPx(context, 24);
		ImageView iv = new ImageView(context);
		iv.setScaleType(ScaleType.CENTER_INSIDE);
		iv.setImageResource(ResHelper.getBitmapRes(context, "umssdk_default_phone"));
		lp = new LayoutParams(dp24, dp24);
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(iv, lp);

		tvCountry = new TextView(context);
		tvCountry.setTextColor(0xff3b3947);
		tvCountry.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		tvCountry.setText("+86");
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = dp24;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(tvCountry, lp);

		View vSep = new View(context);
		vSep.setBackgroundColor(0xffeaeaea);
		lp = new LayoutParams(1, ResHelper.dipToPx(context, 33));
		int dp11 = ResHelper.dipToPx(context, 11);
		lp.rightMargin = dp11;
		lp.leftMargin = dp11;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(vSep, lp);

		etPhone = new EditText(context);
		etPhone.setPadding(0, 0, 0, 0);
		etPhone.setBackground(null);
		etPhone.setSingleLine();
		etPhone.setTextColor(0xff3b3947);
		etPhone.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
		etPhone.setHintTextColor(0xff969696);
		etPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		etPhone.setHint(ResHelper.getStringRes(context, "umssdk_default_enter_phone"));
		etPhone.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(etPhone, lp);

		ivClear = new ImageView(context);
		ivClear.setImageResource(ResHelper.getBitmapRes(context, "umssdk_defalut_clear"));
		ivClear.setScaleType(ScaleType.FIT_XY);
		ivClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etPhone.setText("");
			}
		});
		lp = new LayoutParams((int) etPhone.getTextSize(), (int) etPhone.getTextSize());
		lp.leftMargin = dp11;
		lp.gravity = Gravity.CENTER_VERTICAL;
		ll.addView(ivClear, lp);

		vSep = new View(context);
		vSep.setBackgroundColor(0xffeaeaea);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		addView(vSep, lp);
	}

	public void setPhone(String phone) {
		etPhone.setText(phone);
	}

	public String getPhone() {
		return etPhone.getText().toString().trim();
	}

	public void setCountry(String country) {
		tvCountry.setText("+" + country);
	}

	public String getCountry() {
		return tvCountry.getText().toString().trim().replace("+", "");
	}

}
